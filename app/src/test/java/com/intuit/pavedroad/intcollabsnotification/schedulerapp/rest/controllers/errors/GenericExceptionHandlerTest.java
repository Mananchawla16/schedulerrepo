package com.intuit.pavedroad.intcollabsnotification.schedulerapp.rest.controllers.errors;


import com.intuit.pavedroad.intcollabsnotification.schedulerapp.rest.services.DocumentConstraintViolationException;
import com.intuit.pavedroad.intcollabsnotification.schedulerapp.rest.services.DocumentNotFoundException;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class GenericExceptionHandlerTest {
    @Test
    public void testHandleDocumentException() {
        GenericExceptionHandler genericExceptionHandler = new GenericExceptionHandler();
        DocumentConstraintViolationException documentConstraintViolationException = mock(DocumentConstraintViolationException.class);
        SimpleErrorResponse response = genericExceptionHandler.handleDocumentException(documentConstraintViolationException);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testHandleDocumentNotFoundException() {
        GenericExceptionHandler genericExceptionHandler = new GenericExceptionHandler();
        DocumentNotFoundException documentNotFoundException = mock(DocumentNotFoundException.class);
        SimpleErrorResponse response = genericExceptionHandler.handleDocumentException(documentNotFoundException);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testHandleOtherErrors() {
        GenericExceptionHandler genericExceptionHandler = new GenericExceptionHandler();
        Throwable e = mock(Throwable.class);
        SimpleErrorResponse response = genericExceptionHandler.handleOtherErrors(e);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

}