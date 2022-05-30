package com.intuit.pavedroad.intcollabsnotification.schedulerapp.models;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

@RunWith(SpringJUnit4ClassRunner.class)
public class DocumentsTest {
    @Test
    public void testDocuments() {
        Documents documents = new Documents();
        List<Document> documentList = new ArrayList<>();
        documentList.add(mock(Document.class));
        documentList.add(mock(Document.class));
        documents.setDocuments(documentList);
        assertEquals(2, documents.getDocuments().size());
    }

    @Test
    public void testEquals() {
        Documents docs1 = new Documents();
        Documents docs2 = new Documents();
        List<Document> documentList = new ArrayList<>();
        documentList.add(mock(Document.class));
        documentList.add(mock(Document.class));
        docs1.setDocuments(documentList);
        docs2.setDocuments(documentList);
        assertTrue(docs1.canEqual(docs2));
        assertTrue(docs1.equals(docs2));
        assertEquals(docs1.hashCode(), docs2.hashCode());
        assertEquals(docs1.toString(), docs2.toString());
    }
}
