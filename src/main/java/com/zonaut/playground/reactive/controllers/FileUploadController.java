package com.zonaut.playground.reactive.controllers;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping(value = FileUploadController.API_V_1_FILES)
public class FileUploadController {

    public static final String API_V_1_FILES = "/v1/files";

    //https://github.com/entzik/reactive-spring-boot-examples/blob/master/src/main/java/com/thekirschners/springbootsamples/reactiveupload/ReactiveUploadResource.java

    //https://hantsy.github.io/spring-reactive-sample/web/multipart.html

    // curl -X POST "http://httpbin.org/post" -H "accept: */*" -H "Content-Type: multipart/form-data" -F "file=[object File],[object File]"

    @PostMapping(value = "/files", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    //@Operation(summary = "files")
    public Flux<Void> handleFileUpload(
            @RequestPart("files")
            @Parameter(description = "files", content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE)) Flux<FilePart> filePartFux) throws IOException {
        log.info("start");

        File tmp = File.createTempFile("reactive-upload-", "");
        return filePartFux.flatMap(filePart -> {
            Path path = Paths.get(tmp.toString() + filePart.filename());
            Mono<Void> voidMono = filePart.transferTo(path);
            log.info(path.toString());
            return voidMono;
        });
    }

    /**
     * upload handler method, mapped to POST. Like any file upload handler it consumes MULTIPART_FORM_DATA.
     * Produces a JSON response
     *
     * @param files a flux providing all part contained in the MULTIPART_FORM_DATA request
     * @return a flux of results - one element per uploaded file
     */
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<String> uploadHandler(@RequestPart("files") @Parameter(description = "files", content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE)) Flux<FilePart> files) {
        //public Flux<String> uploadHandler(@RequestBody Flux<Part> files) {
        return files
                .filter(part -> part instanceof FilePart) // only retain file parts
                .ofType(FilePart.class) // convert the flux to FilePart
                .flatMap(this::saveFile); // save each file and flatmap it to a flux of results
    }

    // 2022-10-07 07:51:41.996 -> 2022-10-07 07:51:42.064

    /**
     * tske a {@link FilePart}, transfer it to disk using {@link AsynchronousFileChannel}s and return a {@link Mono} representing the result
     *
     * @param filePart - the request part containing the file to be saved
     * @return a {@link Mono} representing the result of the operation
     */
    private Mono<String> saveFile(FilePart filePart) {
        log.info("handling file upload {}", filePart.filename());

        // if a file with the same name already exists in a repository, delete and recreate it
        final String filename = filePart.filename();
        File file = new File(filename);
        if (file.exists())
            file.delete();
        try {
            file.createNewFile();
        } catch (IOException e) {
            return Mono.error(e); // if creating a new file fails return an error
        }

        try {
            // create an async file channel to store the file on disk
            final AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(file.toPath(), StandardOpenOption.WRITE);

            final CloseCondition closeCondition = new CloseCondition();

            // pointer to the end of file offset
            AtomicInteger fileWriteOffset = new AtomicInteger(0);
            // error signal
            AtomicBoolean errorFlag = new AtomicBoolean(false);

            log.info("subscribing to file parts");
            // FilePart.content produces a flux of data buffers, each need to be written to the file
            return filePart.content().doOnEach(dataBufferSignal -> {
                if (dataBufferSignal.hasValue() && !errorFlag.get()) {
                    // read data from the incoming data buffer into a file array
                    DataBuffer dataBuffer = dataBufferSignal.get();
                    int count = dataBuffer.readableByteCount();
                    byte[] bytes = new byte[count];
                    dataBuffer.read(bytes);

                    // create a file channel compatible byte buffer
                    final ByteBuffer byteBuffer = ByteBuffer.allocate(count);
                    byteBuffer.put(bytes);
                    byteBuffer.flip();

                    // get the current write offset and increment by the buffer size
                    final int filePartOffset = fileWriteOffset.getAndAdd(count);
                    log.info("processing file part at offset {}", filePartOffset);
                    // write the buffer to disk
                    closeCondition.onTaskSubmitted();
                    fileChannel.write(byteBuffer, filePartOffset, null, new CompletionHandler<Integer, ByteBuffer>() {
                        @Override
                        public void completed(Integer result, ByteBuffer attachment) {
                            // file part successfuly written to disk, clean up
                            log.info("done saving file part {}", filePartOffset);
                            byteBuffer.clear();

                            if (closeCondition.onTaskCompleted())
                                try {
                                    log.info("closing after last part");
                                    fileChannel.close();
                                } catch (IOException ignored) {
                                    ignored.printStackTrace();
                                }
                        }

                        @Override
                        public void failed(Throwable exc, ByteBuffer attachment) {
                            // there as an error while writing to disk, set an error flag
                            errorFlag.set(true);
                            log.info("error saving file part {}", filePartOffset);
                        }
                    });
                }
            }).doOnComplete(() -> {
                // all done, close the file channel
                log.info("done processing file parts");
                if (closeCondition.canCloseOnComplete())
                    try {
                        log.info("closing after complete");
                        fileChannel.close();
                    } catch (IOException ignored) {
                    }

            }).doOnError(t -> {
                // ooops there was an error
                log.info("error processing file parts");
                try {
                    fileChannel.close();
                } catch (IOException ignored) {
                }
                // take last, map to a status string
            }).last().map(dataBuffer -> filePart.filename() + " " + (errorFlag.get() ? "error" : "uploaded"));
        } catch (IOException e) {
            // unable to open the file channel, return an error
            log.info("error opening the file channel");
            return Mono.error(e);
        }
    }

    static class CloseCondition {

        AtomicInteger tasksSubmitted = new AtomicInteger(0);
        AtomicInteger tasksCompleted = new AtomicInteger(0);
        AtomicBoolean allTaskssubmitted = new AtomicBoolean(false);

        /**
         * notify all tasks have been subitted, determine of the file channel can be closed
         *
         * @return true if the asynchronous file stream can be closed
         */
        public boolean canCloseOnComplete() {
            allTaskssubmitted.set(true);
            return tasksCompleted.get() == tasksSubmitted.get();
        }

        /**
         * notify a task has been submitted
         */
        public void onTaskSubmitted() {
            tasksSubmitted.incrementAndGet();
        }

        /**
         * notify a task has been completed
         *
         * @return true if the asynchronous file stream can be closed
         */
        public boolean onTaskCompleted() {
            boolean allSubmittedClosed = tasksSubmitted.get() == tasksCompleted.incrementAndGet();
            return allSubmittedClosed && allTaskssubmitted.get();
        }
    }
}
