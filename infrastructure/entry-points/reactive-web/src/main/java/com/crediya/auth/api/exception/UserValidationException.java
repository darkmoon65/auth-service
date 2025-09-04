package com.crediya.auth.api.exception;

import java.util.Map;

public class UserValidationException extends RuntimeException {

    private final Map<String, String> fieldErrors;

    public UserValidationException(String message, Map<String, String> fieldErrors) {
        super(message);
        this.fieldErrors = fieldErrors;
    }

    public Map<String, String> getFieldErrors() {
        return fieldErrors;
    }
}
