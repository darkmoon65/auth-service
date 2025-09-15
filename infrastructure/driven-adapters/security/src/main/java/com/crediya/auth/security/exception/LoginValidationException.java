package com.crediya.auth.security.exception;

public class LoginValidationException extends RuntimeException {
    public LoginValidationException() {
        super("bad credentials");
    }
}
