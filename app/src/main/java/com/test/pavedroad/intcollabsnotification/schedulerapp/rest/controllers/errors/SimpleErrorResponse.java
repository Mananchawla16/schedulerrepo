/**
 * Copyright 2015-2020 Intuit Inc. All rights reserved. Unauthorized reproduction
 * is a violation of applicable law. This material contains certain
 * confidential or proprietary information and trade secrets of Intuit Inc.
 */

package com.test.pavedroad.intcollabsnotification.schedulerapp.rest.controllers.errors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * This class encapsulates all the information provided to API consumers in the
 * event of an error. By extending {@link ResponseEntity}, we can
 * programmatically set the status code on the response.
 *
 * Created by jsk-initializr
 */
public class SimpleErrorResponse extends ResponseEntity<Object> {

    // No status or cause provided, default to 500
    public SimpleErrorResponse(String userFriendlyMessage, boolean scrubSensitiveData) {
        this(HttpStatus.INTERNAL_SERVER_ERROR, userFriendlyMessage, null, scrubSensitiveData);
    }

    // No status provided, default to 500
    public SimpleErrorResponse(String userFriendlyMessage, Throwable cause, boolean scrubSensitiveData) {
        this(HttpStatus.INTERNAL_SERVER_ERROR, userFriendlyMessage, cause, scrubSensitiveData);
    }

    // No cause provided
    public SimpleErrorResponse(HttpStatus status, String userFriendlyMessage, boolean scrubSensitiveData) {
        this(status, userFriendlyMessage, null, scrubSensitiveData);
    }

    public SimpleErrorResponse(HttpStatus status, String userFriendlyMessage, Throwable cause,
            boolean scrubSensitiveData) {
        super(new SimpleErrorResponseBody(userFriendlyMessage, cause, scrubSensitiveData), status);
    }
}