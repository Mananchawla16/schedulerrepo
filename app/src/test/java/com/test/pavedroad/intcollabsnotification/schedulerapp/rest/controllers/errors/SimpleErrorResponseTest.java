package com.test.pavedroad.intcollabsnotification.schedulerapp.rest.controllers.errors;

import com.intuit.pavedroad.intcollabsnotification.schedulerapp.rest.controllers.errors.SimpleErrorResponse;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class SimpleErrorResponseTest {
    @Test
    public void testConstructor() {
        String msg = "friendly msg";
        SimpleErrorResponse simpleErrorResponse = new SimpleErrorResponse(msg, true);
        assertTrue(simpleErrorResponse.getBody().toString().contains(msg));
    }

    @Test
    public void testConstructorWithThrowable() {
        String msg = "friendly msg";
        SimpleErrorResponse simpleErrorResponse = new SimpleErrorResponse(msg, mock(Throwable.class), true);
        assertTrue(simpleErrorResponse.getBody().toString().contains(msg));
    }

    @Test
    public void testConstructorWithStatusCode() {
        String msg = "friendly msg";
        SimpleErrorResponse simpleErrorResponse = new SimpleErrorResponse(HttpStatus.I_AM_A_TEAPOT, msg, true);
        assertTrue(simpleErrorResponse.getBody().toString().contains(msg));
        assertEquals(HttpStatus.I_AM_A_TEAPOT, simpleErrorResponse.getStatusCode());
    }

    @Test
    public void testConstructorWithStatusCodeAndThrowable() {
        String msg = "friendly msg";
        SimpleErrorResponse simpleErrorResponse = new SimpleErrorResponse(HttpStatus.I_AM_A_TEAPOT, msg, mock(Throwable.class), true);
        assertTrue(simpleErrorResponse.getBody().toString().contains(msg));
        assertEquals(HttpStatus.I_AM_A_TEAPOT, simpleErrorResponse.getStatusCode());
    }
}