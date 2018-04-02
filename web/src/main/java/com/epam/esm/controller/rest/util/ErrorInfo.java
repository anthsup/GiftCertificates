package com.epam.esm.controller.rest.util;

public class ErrorInfo {
    private int code;
    private String message;
    public ErrorInfo(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
