package com.zonaut.playground.reactive.exceptions;

import com.zonaut.playground.reactive.exceptions.responses.GlobalErrorMessageTO;
import com.zonaut.playground.reactive.exceptions.responses.GlobalGeneralErrorResponseTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.util.Map;

import static com.zonaut.playground.reactive.exceptions.GlobalErrorUtil.*;
import static java.lang.Integer.parseInt;
import static org.springframework.boot.web.error.ErrorAttributeOptions.Include.*;

@Slf4j
@Component
// The order needs to be lower than DefaultErrorWebExceptionHandler configured in ErrorWebFluxAutoConfiguration
@Order(-2)
public class GlobalErrorWebExceptionHandler extends AbstractErrorWebExceptionHandler {

    public static final ErrorAttributeOptions ERROR_ATTRIBUTE_OPTIONS = ErrorAttributeOptions
            .of(EXCEPTION, STACK_TRACE, MESSAGE, BINDING_ERRORS);

    public GlobalErrorWebExceptionHandler(ErrorAttributes errorAttributes,
                                          ApplicationContext applicationContext,
                                          ServerCodecConfigurer serverCodecConfigurer) {
        super(errorAttributes, new WebProperties.Resources(), applicationContext);
        super.setMessageWriters(serverCodecConfigurer.getWriters());
        super.setMessageReaders(serverCodecConfigurer.getReaders());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    private Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
        final Map<String, Object> originalErrorPropertiesMap = getErrorAttributes(request, ERROR_ATTRIBUTE_OPTIONS);
        for (var entry : originalErrorPropertiesMap.entrySet()) {
            log.trace(entry.getKey() + ": " + entry.getValue());
        }

        HttpStatus httpStatus = HttpStatus.valueOf(parseInt(originalErrorPropertiesMap.get(ERROR_ATTRIBUTE_KEY_STATUS).toString()));

        // Most likely the user requested a path or controller method for a path that does not exist.
        if (httpStatus.is4xxClientError()) {
            GlobalErrorMessageTO errorMessageTO = buildErrorMessageTO("error", "your request is not supported");
            GlobalGeneralErrorResponseTO errorResponseTO = buildErrorResponseTO("4xx_bad_request_general", request, errorMessageTO);
            return ServerResponse.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(errorResponseTO));
        }

        Object exception = originalErrorPropertiesMap.get(ERROR_ATTRIBUTE_KEY_EXCEPTION);
        Object trace = originalErrorPropertiesMap.get(ERROR_ATTRIBUTE_KEY_TRACE);
        log.error("unknown internal WEB error: " + exception + ", " + trace);

        GlobalErrorMessageTO errorMessageTO = buildErrorMessageTO("error", "an unknown error occured");
        GlobalGeneralErrorResponseTO errorResponseTO = buildErrorResponseTO("5xx_unknown_error", request, errorMessageTO);
        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(errorResponseTO));
    }
}