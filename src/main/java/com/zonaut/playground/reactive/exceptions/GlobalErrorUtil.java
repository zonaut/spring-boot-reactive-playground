package com.zonaut.playground.reactive.exceptions;

import com.zonaut.playground.reactive.exceptions.responses.GlobalErrorMessageTO;
import com.zonaut.playground.reactive.exceptions.responses.GlobalGeneralErrorResponseTO;
import org.springframework.validation.FieldError;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.List;
import java.util.stream.Collectors;

public final class GlobalErrorUtil {

    public static final String ERROR_ATTRIBUTE_KEY_TIMESTAMP = "timestamp";
    public static final String ERROR_ATTRIBUTE_KEY_PATH = "path";
    public static final String ERROR_ATTRIBUTE_KEY_STATUS = "status";
    public static final String ERROR_ATTRIBUTE_KEY_ERROR = "error";
    public static final String ERROR_ATTRIBUTE_KEY_MESSAGE = "message";
    public static final String ERROR_ATTRIBUTE_KEY_REQUEST_ID = "requestId";
    public static final String ERROR_ATTRIBUTE_KEY_EXCEPTION = "exception";
    public static final String ERROR_ATTRIBUTE_KEY_TRACE = "trace";

    private GlobalErrorUtil() {
    }

    public static GlobalErrorMessageTO buildErrorMessageTO(String name, String message) {
        return GlobalErrorMessageTO.builder().name(name).message(message).build();
    }

    public static GlobalGeneralErrorResponseTO buildErrorResponseTO(String code, ServerRequest request, GlobalErrorMessageTO errorMessage) {
        return GlobalGeneralErrorResponseTO.builder()
                .code(code)
                .traceId(request.exchange().getRequest().getId())
                .message(errorMessage)
                .build();
    }

    public static List<GlobalErrorMessageTO> buildFieldErrorMessages(List<FieldError> fieldErrors) {
        return fieldErrors
                .stream()
                .map(fieldError -> buildErrorMessageTO(fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.toList());
    }

    public static List<GlobalErrorMessageTO> buildInvalidErrorMessages(String name, String message) {
        return List.of(buildErrorMessageTO(name, message));
    }
}
