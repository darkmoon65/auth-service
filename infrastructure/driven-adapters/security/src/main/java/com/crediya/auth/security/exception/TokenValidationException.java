package com.crediya.auth.security.exception;

public class TokenValidationException extends RuntimeException {
    public TokenValidationException() {
        super("Unauthorized");
    }
}
