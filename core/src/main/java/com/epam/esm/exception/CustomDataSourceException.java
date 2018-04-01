package com.epam.esm.exception;

public class CustomDataSourceException extends RuntimeException {
    public CustomDataSourceException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public CustomDataSourceException(Throwable throwable) {
        super(throwable);
    }

    public CustomDataSourceException(String s) {
        super(s);
    }
}
