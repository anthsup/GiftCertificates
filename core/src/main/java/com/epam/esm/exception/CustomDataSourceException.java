package com.epam.esm.exception;

public class CustomDataSourceException extends GiftCertificatesException {
    public CustomDataSourceException(Throwable throwable) {
        super(throwable);
    }

    public CustomDataSourceException(String s) {
        super(s);
    }
}
