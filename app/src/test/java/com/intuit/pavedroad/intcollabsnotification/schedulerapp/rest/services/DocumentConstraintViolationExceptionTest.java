package com.intuit.pavedroad.intcollabsnotification.schedulerapp.rest.services;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DocumentConstraintViolationExceptionTest {

    @Test
    public void testConstructor() {
        DocumentConstraintViolationException documentConstraintViolationException = new DocumentConstraintViolationException();
        assertNotNull(documentConstraintViolationException);
    }

    @Test
    public void testConstructorWithString() {
        String msg = "this is an exception";
        DocumentConstraintViolationException documentConstraintViolationException = new DocumentConstraintViolationException(msg);
        assertEquals(msg, documentConstraintViolationException.getMessage());
    }

    @Test
    public void testConstructorWithThrowable() {
        DocumentConstraintViolationException documentConstraintViolationException = new DocumentConstraintViolationException(new Throwable());
        assertNotNull(documentConstraintViolationException);
    }

    @Test
    public void testConstructorWithStringAndThrowable() {
        String msg = "whaaaat";
        DocumentConstraintViolationException documentConstraintViolationException = new DocumentConstraintViolationException(msg, new Throwable());
        assertEquals(msg, documentConstraintViolationException.getMessage());
    }
}