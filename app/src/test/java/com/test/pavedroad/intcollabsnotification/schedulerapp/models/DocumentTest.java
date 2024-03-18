package com.test.pavedroad.intcollabsnotification.schedulerapp.models;

import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentTest {
    @Test
    public void testDocumentEquals() {
        Document doc1 = new Document();
        Document doc2 = new Document();
        Document doc3 = new Document();
        doc3.setContent("doc 3");
        assertEquals(doc1.hashCode(), doc2.hashCode());
        assertEquals(doc1.toString(), doc2.toString());
        assertTrue(doc1.canEqual(doc2));
        assertTrue(doc1.equals(doc2));
        assertNotEquals(doc1, doc3);
    }
}
