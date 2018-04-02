package com.epam.esm.exception;

public class CustomDataSourceException extends RuntimeException {
    public CustomDataSourceException(Throwable throwable) {
        super(throwable);
    }

    public CustomDataSourceException(String s) {
        super(s);
    }
}
