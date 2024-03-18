package com.test.pavedroad.intcollabsnotification.schedulerapp.rest.controllers;

import com.test.pavedroad.intcollabsnotification.schedulerapp.models.Document;
import com.test.pavedroad.intcollabsnotification.schedulerapp.rest.services.DocumentService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class DocumentControllerTest {
    @InjectMocks
    private DocumentController documentController;

    @Mock
    private DocumentService documentService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetDocuments() {
        assertNull(documentController.getDocuments());
    }

    @Test
    public void testCreateDocument() {
        Document mockDoc = mock(Document.class);
        documentController.createDocument(mockDoc);
        verify(documentService).createDocument(mockDoc);
    }

    @Test
    public void testGetDocument() {
        documentController.getDocument("123");
        verify(documentService).getDocument("123");
    }

    @Test
    public void testUpdateDocument() {
        Document mockDoc = mock(Document.class);
        documentController.updateDocument("123", mockDoc);
        verify(documentService).updateDocument(mockDoc, "123");
    }

    @Test
    public void testDeleteDocument() {
        documentController.deleteDocument("123");
        verify(documentService).deleteDocument("123");
    }
}
