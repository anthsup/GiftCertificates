package com.epam.esm.exception;

public class ClosingConnectionException extends RuntimeException {
    public ClosingConnectionException(Throwable throwable) {
        super(throwable);
    }
}
