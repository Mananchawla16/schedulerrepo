package com.intuit.pavedroad.intcollabsnotification.schedulerapp.rest.controllers;

import com.intuit.pavedroad.intcollabsnotification.schedulerapp.models.Document;
import com.intuit.pavedroad.intcollabsnotification.schedulerapp.rest.services.DocumentService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.intuit.platform.jsk.security.iam.authn.IntuitTicketAuthentication;

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
        assertNull(documentController.getDocuments(mock(IntuitTicketAuthentication.class)));
    }

    @Test
    public void testCreateDocument() {
        Document mockDoc = mock(Document.class);
        documentController.createDocument(mockDoc, mock(IntuitTicketAuthentication.class));
        verify(documentService).createDocument(mockDoc);
    }

    @Test
    public void testGetDocument() {
        documentController.getDocument("123", mock(IntuitTicketAuthentication.class));
        verify(documentService).getDocument("123");
    }

    @Test
    public void testUpdateDocument() {
        Document mockDoc = mock(Document.class);
        documentController.updateDocument("123", mockDoc, mock(IntuitTicketAuthentication.class));
        verify(documentService).updateDocument(mockDoc, "123");
    }

    @Test
    public void testDeleteDocument() {
        documentController.deleteDocument("123", mock(IntuitTicketAuthentication.class));
        verify(documentService).deleteDocument("123");
    }
}
