package com.flower.util.exception.custom;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException(final String message) {
        super(message);
    }
}
