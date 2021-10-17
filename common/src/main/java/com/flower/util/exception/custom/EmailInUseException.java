package com.flower.util.exception.custom;

public class EmailInUseException extends RuntimeException {
    public EmailInUseException(final String message) {
        super(message);
    }
}