/**
 * Copyright 2015-2020 Intuit Inc. All rights reserved. Unauthorized reproduction
 * is a violation of applicable law. This material contains certain
 * confidential or proprietary information and trade secrets of Intuit Inc.
 */
package com.intuit.pavedroad.intcollabsnotification.schedulerapp.rest.controllers.errors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.intuit.pavedroad.intcollabsnotification.schedulerapp.rest.services.DocumentConstraintViolationException;
import com.intuit.pavedroad.intcollabsnotification.schedulerapp.rest.services.DocumentNotFoundException;

@ResponseBody
@ControllerAdvice
public class GenericExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GenericExceptionHandler.class);

    @Value("${server.include-debug-info}")
    private boolean includeDebugInfo;

    @ExceptionHandler
    public SimpleErrorResponse handleDocumentException(DocumentConstraintViolationException e) {
        LOGGER.error("handling an error from the document service", e);
        return genericErrorResponse(HttpStatus.BAD_REQUEST, "Incomplete values in the request", e);
    }

    @ExceptionHandler
    public SimpleErrorResponse handleDocumentException(DocumentNotFoundException e) {
        LOGGER.error("handling an error from the document service", e);
        return genericErrorResponse(HttpStatus.NOT_FOUND, "Resource not found " + e.getDocumentId(), e);
    }

    @ExceptionHandler
    public SimpleErrorResponse handleOtherErrors(Throwable t) {
        LOGGER.error("handling an unexpected exception", t);
        return genericErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred processing this request", t);
    }

    private SimpleErrorResponse genericErrorResponse(HttpStatus status, String message, Throwable e) {
        return new SimpleErrorResponse(status, message, e, includeDebugInfo);
    }
}
