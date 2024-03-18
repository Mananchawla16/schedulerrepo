package com.test.pavedroad.intcollabsnotification.schedulerapp.rest.services;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DocumentNotFoundExceptionTest {
    @Test
    public void testGetDocumentId() {
        String someId = "my id";
        DocumentNotFoundException documentNotFoundException = new DocumentNotFoundException(someId);
        assertEquals(someId, documentNotFoundException.getDocumentId());
    }
}