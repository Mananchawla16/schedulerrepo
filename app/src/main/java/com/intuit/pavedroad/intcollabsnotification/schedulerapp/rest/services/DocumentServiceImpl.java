/**
 * Copyright 2015-2020 Intuit Inc. All rights reserved. Unauthorized reproduction
 * is a violation of applicable law. This material contains certain
 * confidential or proprietary information and trade secrets of Intuit Inc.
 */
package com.intuit.pavedroad.intcollabsnotification.schedulerapp.rest.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.intuit.pavedroad.intcollabsnotification.schedulerapp.models.Document;
import com.intuit.pavedroad.intcollabsnotification.schedulerapp.models.Documents;

/**
 * This implementation of {@code DocumentService} uses a concurrent hash map as
 * an in-memory store.
 * 
 * @author koppenheim
 *
 */
@Service
public class DocumentServiceImpl implements DocumentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentServiceImpl.class);

    private Map<String, Document> documents = new ConcurrentHashMap<>();

    @Resource
    private DateTimeService dateTimeService;

    @Override
    public Documents getDocuments() {
        Documents docList = new Documents();
        List<Document> docs = new ArrayList<>(documents.values());
        docList.setDocuments(docs);
        return docList;
    }

    @Override
    public Document getDocument(String documentId) {

        Document document = documents.get(documentId);
        if (document == null) {
            throw new DocumentNotFoundException(documentId);
        }

        return document;
    }

    @Override
    public Document createDocument(Document document) {
        if (document == null) {
            LOGGER.error("createDocument: document parameter is null!");
            throw new DocumentConstraintViolationException("createDocument: Document must not be null");
        } else if (StringUtils.isBlank(document.getContent())) {
            LOGGER.error("createDocument: document parameter must have non-empty content; document=" + document);
            throw new DocumentConstraintViolationException(
                    "createDocument: Document must have non-empty content, but document=" + document);
        }

        // set the time stamps in the document
        Date now = dateTimeService.getDateTime();
        document.setCreatedDate(now);
        document.setLastModifiedDate(now);

        document.setId(RandomStringUtils.randomNumeric(10));

        try {
            documents.put(document.getId(), document);
        } catch (OutOfMemoryError oome) {
            LOGGER.error("createDocument: got OutOfMemoryError");
            LOGGER.error("createDocument: documents.size=" + documents.size());
            throw oome;
        }
        return document;
    }

    @Override
    public Document updateDocument(Document document, String documentId) {

        validateDocument(document);

        validateDocumentId(document, documentId);

        Document updatingDocument = getDocument(documentId);

        if (updatingDocument == null) {
            throw new DocumentNotFoundException(documentId);
        }

        updatingDocument.setLastModifiedDate(dateTimeService.getDateTime());
        updatingDocument.setContent(document.getContent());

        documents.put(documentId, updatingDocument);

        return updatingDocument;
    }

    @Override
    public void deleteDocument(String documentId) {
        Document document = documents.remove(documentId);
        if (document == null) {
            throw new DocumentNotFoundException(documentId);
        }
    }

    private void validateDocumentId(Document document, String documentId) {
        if (StringUtils.isEmpty(documentId)) {
            throw new DocumentConstraintViolationException(
                    "documentId must not be null or empty, document=" + document + ", documentId=" + documentId);
        }

        if (!StringUtils.isEmpty(document.getId())) {
            if (!document.getId().equals(documentId)) {
                throw new DocumentConstraintViolationException("if document ID is present, it must equal the documentId"
                        + " argument, " + "document=" + document + ", documentId=" + documentId);
            }
        }
    }

    private void validateDocument(Document document) {
        if (document == null || StringUtils.isBlank(document.getContent())) {
            throw new DocumentConstraintViolationException(
                    "Document must not be null and must have non-empty content, document=" + document);
        }
    }

}
