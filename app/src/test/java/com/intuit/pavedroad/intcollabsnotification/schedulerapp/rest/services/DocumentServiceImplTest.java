package com.intuit.pavedroad.intcollabsnotification.schedulerapp.rest.services;


import com.intuit.pavedroad.intcollabsnotification.schedulerapp.models.Document;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertNotNull;


public class DocumentServiceImplTest {

    @InjectMocks
    DocumentServiceImpl documentService;

    @Mock
    private DateTimeService dateTimeService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetCreateDocuments() {
        Document document = new Document();
        document.setContent("hello this is doc 1.");
        documentService.createDocument(document);

        Document document2 = new Document();
        document2.setContent("hello this is doc 2.");
        documentService.createDocument(document2);

        assertNotNull(documentService.getDocuments());
    }

    @Test(expected = DocumentConstraintViolationException.class)
    public void testCreateNullDocument() {
        DocumentServiceImpl documentService = new DocumentServiceImpl();
        documentService.createDocument(null);
    }

    @Test(expected = DocumentConstraintViolationException.class)
    public void testCreateBlankContentDocument() {
        DocumentServiceImpl documentService = new DocumentServiceImpl();
        Document document = new Document();
        documentService.createDocument(document);
    }

    @Test(expected = DocumentConstraintViolationException.class)
    public void testValidateDocumentIdEmptyId() {
        DocumentServiceImpl documentService = new DocumentServiceImpl();
        Document document = new Document();
        document.setContent("hello this is content.");
        documentService.updateDocument(document, "");
    }

    @Test(expected = DocumentConstraintViolationException.class)
    public void testValidateDocumentIdDifferentId() {
        DocumentServiceImpl documentService = new DocumentServiceImpl();
        Document document = new Document();
        document.setId("123");
        document.setContent("hello this is content.");
        documentService.updateDocument(document, "qqq");
    }
}