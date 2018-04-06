package com.epam.esm.controller.rest;

import com.epam.esm.controller.rest.util.ErrorInfo;
import com.epam.esm.exception.EmptyRequestBodyException;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice("com.epam.esm.controller.rest")
public class RestExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(RestExceptionHandler.class);

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

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public ErrorInfo handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e) {
        LOGGER.debug(e.getMessage(), e);
        return new ErrorInfo(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(),
                "The server doesn't support this media type");
    }

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ErrorInfo handleHttpMediaTypeNotAcceptableException(HttpMediaTypeNotAcceptableException e) {
        LOGGER.debug(e.getMessage(), e);
        return new ErrorInfo(HttpStatus.NOT_ACCEPTABLE.value(),
                "Unfortunately you don't support the media type which the server produces.");
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ErrorInfo handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        LOGGER.debug(e.getMessage(), e);
        return new ErrorInfo(HttpStatus.METHOD_NOT_ALLOWED.value(),
                "This HTTP method is not supported for the given URI.");
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorInfo handleNoHandlerFoundException(NoHandlerFoundException e) {
        LOGGER.debug(e.getMessage(), e);
        return new ErrorInfo(HttpStatus.NOT_FOUND.value(),
                "There is no handler for the given URI");
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorInfo defaultHandleException(Throwable e) {
        LOGGER.debug(e.getMessage(), e);
        return new ErrorInfo(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Something unexpected happened during the request, we're already working on it.");
    }
}
