package com.epam.esm.exception;

public class EmptyRequestBodyException extends RuntimeException {
    public EmptyRequestBodyException(String message) {
        super(message);
    }
}
