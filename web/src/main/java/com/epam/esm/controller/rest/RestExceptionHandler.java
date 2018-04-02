package com.epam.esm.controller.rest;

import com.epam.esm.controller.rest.util.ErrorInfo;
import com.epam.esm.exception.EmptyRequestBodyException;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.exception.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice("com.epam.esm.controller.rest")
public class RestExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorInfo resourceNotFound(Exception e) {
        return new ErrorInfo(HttpStatus.NOT_FOUND.value(), e.getMessage());
    }

    @ExceptionHandler({ValidationException.class, EmptyRequestBodyException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorInfo invalidParametersError(Exception e) {
        return new ErrorInfo(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }
}
