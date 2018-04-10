package com.epam.esm.exception;

class GiftCertificatesException extends RuntimeException {
    GiftCertificatesException(String message) {
        super(message);
    }

    GiftCertificatesException(Throwable cause) {
        super(cause);
    }
}
