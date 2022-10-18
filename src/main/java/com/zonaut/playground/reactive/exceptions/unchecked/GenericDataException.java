package com.zonaut.playground.reactive.exceptions.unchecked;

public class GenericDataException extends RuntimeException {

    public GenericDataException() {
    }

    public GenericDataException(String message) {
        super(message);
    }

    public GenericDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public GenericDataException(Throwable cause) {
        super(cause);
    }

    public GenericDataException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
