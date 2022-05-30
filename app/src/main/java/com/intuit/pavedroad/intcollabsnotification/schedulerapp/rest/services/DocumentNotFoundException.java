/**
 * Copyright 2015-2020 Intuit Inc. All rights reserved. Unauthorized reproduction
 * is a violation of applicable law. This material contains certain
 * confidential or proprietary information and trade secrets of Intuit Inc.
 */
package com.intuit.pavedroad.intcollabsnotification.schedulerapp.rest.services;

/**
 * Models a document not found error.
 * 
 * @author koppenheim
 *
 */
public class DocumentNotFoundException extends DocumentServiceException {

    private static final long serialVersionUID = 1L;

    private final String documentId;

    /**
     * Construct with a message {@code String} that is returned by the inherited
     * {@code Throwable.getMessage}.
     *
     * @param message
     *            the message that is returned by the inherited
     *            {@code Throwable.getMessage}
     * @param documentId
     *            the id of the missing document
     */
    public DocumentNotFoundException(String documentId) {
        super("Failed to find document for documentId=" + documentId);
        this.documentId = documentId;
    }

    public String getDocumentId() {
      return documentId;
    }
}
