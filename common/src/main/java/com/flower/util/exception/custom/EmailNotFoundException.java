package com.flower.util.exception.custom;

public class EmailNotFoundException extends RuntimeException {
    public EmailNotFoundException(final String message) {
        super(message);
    }
}