package com.epam.esm.controller.rest.util;

import com.epam.esm.domain.GiftCertificate;
import com.epam.esm.exception.EmptyRequestBodyException;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.exception.ValidationException;

public class RestPreConditions {
    private RestPreConditions() {
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

    public static void updatedEntityIdValid(long certificateId, GiftCertificate certificate) {
        if (certificateId != certificate.getId()) {
            throw new ValidationException("Path and request body id should match.");
        }
    }
}
