package com.intuit.pavedroad.intcollabsnotification.schedulerapp.rest.controllers.errors;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class SimpleErrorResponseBodyTest {
    @Test
    public void testSimpleErrorResponseBody() {
        String msg = "this is a msg";
        Throwable cause = mock(Throwable.class);
        SimpleErrorResponseBody simpleErrorResponseBody = new SimpleErrorResponseBody(msg, cause, true);
        simpleErrorResponseBody.setMessage("new message");
        simpleErrorResponseBody.setDebugInfo("debug info");

        SimpleErrorResponseBody simpleErrorResponseBody2 = new SimpleErrorResponseBody(msg, cause, true);
        simpleErrorResponseBody2.setMessage("new message");
        simpleErrorResponseBody2.setDebugInfo("debug info");

        assertTrue(simpleErrorResponseBody.equals(simpleErrorResponseBody2));
        assertTrue(simpleErrorResponseBody.canEqual(simpleErrorResponseBody2));
        assertEquals(simpleErrorResponseBody.hashCode(), simpleErrorResponseBody2.hashCode());
    }
}
