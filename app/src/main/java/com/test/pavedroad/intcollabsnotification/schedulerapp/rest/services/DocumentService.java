package com.test.pavedroad.intcollabsnotification.schedulerapp.rest.services;

import com.test.pavedroad.intcollabsnotification.schedulerapp.models.Document;
import com.test.pavedroad.intcollabsnotification.schedulerapp.models.Documents;

/**
 * Provides service tier CRUD operations for {@code Document}.
 * 
 * @author koppenheim
 *
 */
public interface DocumentService {

    /**
     * Get all the {@code Document}s known to this service.
     *
     * @return all {@code Document}s known to this service
     */
    public Documents getDocuments();

    /**
     * Get the {@code Document} associated with {@code documentId}
     *
     * @param documentId
     *            identifier for requested {@code Document}
     * @return {@code Document} associated with {@code documentId}
     * @throws DocumentNotFoundException
     *             if no {@code Document} found for {@code documentId}
     */
    public Document getDocument(String documentId);

    /**
     * Create a new {@code Document} with auto-generated ID. The
     * {@code creationDate} and {@code lastModifiedDate} are set to the current
     * date and time modeled as UTC milliseconds.
     *
     * @param document
     *            {@code Document} to create
     * @throws DocumentConstraintViolationException
     *             if the following preconditions aren't satisfied:
     *             <ul>
     *             <li>{@code document} must not be null</li>
     *             <li>{@code document.content} must not be null or empty</li>
     *             </ul>
     * @return {@code Document} associated with {@code Document}
     */
    public Document createDocument(Document document);

    /**
     * Updates {@code Document} to reflect input {@code document}.
     *
     * @param document
     *            specifies required {@code Document} state
     * @param documentId
     *            specifies the updating {@code Document}'s identifier
     * @return updated {@code Document}
     * @throws DocumentConstraintViolationException
     *             if the following preconditions aren't satisfied:
     *             <ul>
     *             <li>{@code documentId} must not be null or empty</li>
     *             <li>if {@code document.id} is not null or empty, it must
     *             equal {@code documentId}</li>
     *             <li>{@code document} must not be null</li>
     *             <li>{@code document.content} must not be null or empty</li>
     *             </ul>
     * @throws DocumentNotFoundException
     *             if no {@code Document} found for {@code document.id}
     */
    public Document updateDocument(Document document, String documentId);

    /**
     * Delete {@code Document} associated with {@code documentId}
     *
     * @param documentId
     *            identifier for deleting {@code Document}
     * @throws DocumentNotFoundException
     *             if {@code Document} associated with {@code documentId}
     *             doesn't exist
     */
    public void deleteDocument(String documentId);

}
