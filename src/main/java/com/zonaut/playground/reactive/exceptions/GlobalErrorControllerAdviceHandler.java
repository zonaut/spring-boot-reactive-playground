package com.zonaut.playground.reactive.exceptions;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.zonaut.playground.reactive.exceptions.responses.GlobalErrorMessageTO;
import com.zonaut.playground.reactive.exceptions.responses.GlobalGeneralErrorResponseTO;
import com.zonaut.playground.reactive.exceptions.responses.GlobalValidationErrorResponseTO;
import com.zonaut.playground.reactive.exceptions.unchecked.GenericDataException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.zonaut.playground.reactive.exceptions.GlobalErrorUtil.*;

@Slf4j
@AllArgsConstructor
@ControllerAdvice
public class GlobalErrorControllerAdviceHandler {

    @ExceptionHandler
    public Mono<ResponseEntity<GlobalGeneralErrorResponseTO>> gottaCatchThemAll(Exception e, ServerWebExchange ex) {
        log.error("unknown internal error", e);

        GlobalErrorMessageTO errorMessageTO = buildErrorMessageTO("error",
                "An unknown problem occurred, our people are already getting notified about this problem.");
        GlobalGeneralErrorResponseTO errorResponseTO = GlobalGeneralErrorResponseTO.builder()
                .code("5xx_unknown_error")
                .traceId(ex.getRequest().getId())
                .message(errorMessageTO)
                .build();

        return Mono.just(ResponseEntity.internalServerError().body(errorResponseTO));
    }

    @ExceptionHandler({WebExchangeBindException.class})
    public Mono<ResponseEntity<GlobalValidationErrorResponseTO>> handleException(WebExchangeBindException e, ServerWebExchange ex) {
        // TODO translate into pretty messages and hide internals
        List<GlobalErrorMessageTO> errorMessagesTO = buildFieldErrorMessages(e.getBindingResult().getFieldErrors());
        GlobalValidationErrorResponseTO errorResponseTO = GlobalValidationErrorResponseTO.builder()
                .code("4xx_validation_general")
                .traceId(ex.getRequest().getId())
                .messages(errorMessagesTO)
                .build();

        return Mono.just(ResponseEntity.badRequest().body(errorResponseTO));
    }

    @ExceptionHandler({ServerWebInputException.class})
    public Mono<ResponseEntity<GlobalValidationErrorResponseTO>> handleInvalidWebInput(Exception e, ServerWebExchange ex) {
        log.error("web input error, check to make sure it isn't something internal, probably wrong json formatting", e);

        // TODO translate into pretty messages and hide internals
        List<GlobalErrorMessageTO> errorMessagesTO = buildInvalidErrorMessages("invalid", e.getMessage());
        GlobalValidationErrorResponseTO errorResponseTO = GlobalValidationErrorResponseTO.builder()
                .code("4xx_validation_invalid_input")
                .traceId(ex.getRequest().getId())
                .messages(errorMessagesTO)
                .build();

        return Mono.just(ResponseEntity.badRequest().body(errorResponseTO));
    }

    @ExceptionHandler(InvalidFormatException.class)
    public Mono<ResponseEntity<GlobalValidationErrorResponseTO>> handleException(InvalidFormatException e, ServerWebExchange ex) {
        // TODO translate into pretty messages and hide internals
        List<GlobalErrorMessageTO> errorMessagesTO = buildInvalidErrorMessages("invalid", e.getMessage());
        GlobalValidationErrorResponseTO errorResponseTO = GlobalValidationErrorResponseTO.builder()
                .code("4xx_validation_invalid_data")
                .traceId(ex.getRequest().getId())
                .messages(errorMessagesTO)
                .build();

        return Mono.just(ResponseEntity.badRequest().body(errorResponseTO));
    }

    @ExceptionHandler(GenericDataException.class)
    public Mono<ResponseEntity<GlobalValidationErrorResponseTO>> handleException(GenericDataException e, ServerWebExchange ex) {
        // TODO translate into pretty messages and hide internals
        List<GlobalErrorMessageTO> errorMessagesTO = buildInvalidErrorMessages("error", e.getMessage());
        GlobalValidationErrorResponseTO errorResponseTO = GlobalValidationErrorResponseTO.builder()
                .code("4xx_validation_data")
                .traceId(ex.getRequest().getId())
                .messages(errorMessagesTO)
                .build();

        return Mono.just(ResponseEntity.badRequest().body(errorResponseTO));
    }
}
