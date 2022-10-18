package com.zonaut.playground.reactive.controllers;

import com.zonaut.playground.reactive.controllers.events.ProductEvent;
import com.zonaut.playground.reactive.controllers.requests.ProductCreateRequestTO;
import com.zonaut.playground.reactive.controllers.requests.ProductUpdateRequestTO;
import com.zonaut.playground.reactive.controllers.responses.ProductResponseTO;
import com.zonaut.playground.reactive.entities.ProductEntity;
import com.zonaut.playground.reactive.entities.ProductEntityFeaturesJson;
import com.zonaut.playground.reactive.mappers.ProductObjectMapper;
import com.zonaut.playground.reactive.repositories.ProductRepository;
import io.netty.handler.codec.http.HttpHeaderNames;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.time.Duration;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static com.zonaut.playground.reactive.mappers.ProductObjectMapper.mapProductUpdateRequestTOToProductEntity;
import static java.time.temporal.ChronoUnit.MILLIS;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.*;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping(value = ProductController.API_V_1_PRODUCTS)
public class ProductController {

    public static final String API_V_1_PRODUCTS = "/v1/products";
    public static final ProductEntityFeaturesJson PRODUCT_ENTITY_FEATURES_JSON = ProductEntityFeaturesJson.builder()
            .feature1("1".repeat(5))
            .feature2("2".repeat(5))
            .build();

    private final ProductRepository productRepository;

    // https://stackoverflow.com/questions/52098863/whats-the-difference-between-text-event-stream-and-application-streamjson
    // https://nurkiewicz.com/2021/08/json-streaming-in-webflux.html
    // TODO check LISTEN on PostgreSQL for live updates on tables
    //      - https://stackoverflow.com/questions/21632243/how-do-i-get-asynchronous-event-driven-listen-notify-support-in-java-using-a-p
    //      - https://github.com/pictet-technologies-open-source/reactive-todo-list-r2dbc

    // http://localhost:8080/api/v1/products
    // curl --header "Accept: application/json" -v http://localhost:8080/api/v1/products
    // curl --header "Accept: application/x-ndjson" -v http://localhost:8080/api/v1/products
    // curl --header "Accept: text/event-stream" -v http://localhost:8080/api/v1/products
    @GetMapping(produces = {APPLICATION_JSON_VALUE, APPLICATION_NDJSON_VALUE, TEXT_EVENT_STREAM_VALUE})
    public Flux<ProductResponseTO> getAllProducts(@RequestHeader Map<String, String> headers) {
        log.info("getAllProducts called");

        Flux<ProductEntity> flux = productRepository.findAll();
        String acceptHeader = headers.get("accept") != null ? headers.get("accept") : headers.get("Accept");
        if (Set.of(APPLICATION_NDJSON_VALUE, TEXT_EVENT_STREAM_VALUE).contains(acceptHeader)) {
            return flux
                    .delayElements(Duration.of(250, MILLIS))
                    .flatMap(ProductObjectMapper::mapProductEntityToProductResponseTOMono);
        }
        return flux.flatMap(ProductObjectMapper::mapProductEntityToProductResponseTOMono);
    }

    // http://localhost:8080/api/v1/products/events
    @GetMapping(value = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ProductEvent> getProductEvents() {
        return Flux.interval(Duration.ofSeconds(1))
                .map(x -> new ProductEvent(x, "Product Event"));
    }

    // http://localhost:8080/api/v1/products/filter?page=0&size=3
    // TODO https://prateek-ashtikar512.medium.com/r2dbc-query-by-example-9553e86f6fd6 for searching
    @GetMapping(path = "/filter")
    public Mono<Page<ProductResponseTO>> getAllProductsOnFilter(@RequestParam("page") int page,
                                                                @RequestParam("size") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return this.productRepository.findAllBy(pageRequest.withSort(Sort.by("price").descending()))
                .collectList()
                .zipWith(this.productRepository.count())
                .map(t -> new PageImpl<>(t.getT1().stream().map(ProductObjectMapper::mapProductEntityToProductResponseTO).toList(), pageRequest, t.getT2()));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<ProductEntity>> getProductById(@PathVariable("id") UUID id) {
        log.info("getProductById called for id {}", id);

        return productRepository.findById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping()
    @ResponseStatus(CREATED)
    public Mono<ProductResponseTO> createProduct(@Valid @RequestBody ProductCreateRequestTO createProduct) {
        log.info("createProduct called with {}", createProduct);

        ProductEntity customer = ProductObjectMapper.mapProductCreateRequestTOToProductEntity(createProduct);
        return productRepository.save(customer)
                .flatMap(ProductObjectMapper::mapProductEntityToProductResponseTOMono);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<ProductResponseTO>> updateProduct(@Valid @RequestBody ProductUpdateRequestTO updateProduct, @PathVariable UUID id) {
        log.info("updateProduct called with {} for id {}", updateProduct, id);

        return productRepository.findById(id)
                .flatMap(existingProduct -> {
                    mapProductUpdateRequestTOToProductEntity(existingProduct, updateProduct);
                    return productRepository.save(existingProduct).flatMap(ProductObjectMapper::mapProductEntityToProductResponseTOMono);
                })
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteProduct(@PathVariable UUID id) {
        log.info("deleteProduct called for id {}", id);

        return productRepository.findById(id)
                .flatMap(product -> productRepository.delete(product)
                        .then(Mono.just(ResponseEntity.ok().<Void>build()))
                )
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
