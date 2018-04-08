package com.epam.esm.controller.rest.util;

import com.epam.esm.exception.EmptyRequestBodyException;
import com.epam.esm.exception.ResourceNotFoundException;

public class RestValidator {
    private RestValidator() {
    }

    public static <T> T checkFound(T resource) {
        if (resource == null) {
            throw new ResourceNotFoundException("Couldn't find resource you are looking for.");
        }
        return resource;
    }

    public static <T> void checkNotNull(T value) {
        if (null == value) {
            throw new EmptyRequestBodyException("Request body is empty!");
        }
    }
}
