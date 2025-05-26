package com.deepread.exception;

public class FlaskApiException extends RuntimeException {
    public FlaskApiException(String message) {
        super(message);
    }

    public FlaskApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
