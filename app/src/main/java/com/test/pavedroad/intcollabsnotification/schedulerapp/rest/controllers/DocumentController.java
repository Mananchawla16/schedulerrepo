/**
 * Copyright 2015-2020 Intuit Inc. All rights reserved. Unauthorized reproduction
 * is a violation of applicable law. This material contains certain
 * confidential or proprietary information and trade secrets of Intuit Inc.
 */
package com.test.pavedroad.intcollabsnotification.schedulerapp.rest.controllers;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.test.pavedroad.intcollabsnotification.schedulerapp.models.Document;
import com.test.pavedroad.intcollabsnotification.schedulerapp.models.Documents;
import com.test.pavedroad.intcollabsnotification.schedulerapp.rest.services.DocumentService;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping(value = "v1/documents", produces = { MediaType.APPLICATION_JSON_VALUE })
@Tag(name = "documents")
public class DocumentController {

    @Resource
    private DocumentService documentService;

    @RequestMapping(method = RequestMethod.GET)
	@Operation(description = "Get all the documents", summary = "Get all the documents", security = {
			@SecurityRequirement(name = "privateAuthUser", scopes = {}) })
	public Documents getDocuments() {
		return documentService.getDocuments();
	}

	@RequestMapping(method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE })
	@Operation(description = "Create a new document that has content", summary = "Creates a document record. DocumentId is dynamically generated. "
			+ "The userName is set to the logged in user name from iamPrincipal.", security = {
					@SecurityRequirement(name = "privateAuthUser", scopes = {}) })
	@ApiResponses(value = { @ApiResponse(responseCode = "400", description = "Bad request") })
	public Document createDocument(
			@RequestBody @Parameter(description = "Document object that contains a content field", required = true, example = "{\"content\":\"This is the sample document content\"}") Document document) {

//		if (principal != null) {
//			document.setUserName(principal.getName());
//		}

		Document newDocument = documentService.createDocument(document);
		return newDocument;
	}

	@RequestMapping(value = "{documentId}", method = RequestMethod.GET)
	@Operation(description = "Get a document by document ID", summary = "Get a document by document ID", security = {
			@SecurityRequirement(name = "privateAuthUser", scopes = {}) })
	@ApiResponses(value = { @ApiResponse(responseCode = "404", description = "Not found") })
	public Document getDocument(
			@PathVariable @Parameter(description = "The document ID that was returned in the document object from the POST command", required = true) String documentId) {
		return documentService.getDocument(documentId);
	}

	@RequestMapping(value = "{documentId}", method = RequestMethod.PUT, consumes = { MediaType.APPLICATION_JSON_VALUE })
	@Operation(description = "Update document's content based on the document ID", summary = "Update document's content based on the document ID", security = {
			@SecurityRequirement(name = "privateAuthUser", scopes = {}) })
	@ApiResponses(value = { @ApiResponse(responseCode = "400", description = "Bad request"),
			@ApiResponse(responseCode = "404", description = "Not found") })
	public Document updateDocument(
			@PathVariable @Parameter(description = "The document ID that was returned in the document object from the POST command", required = true) String documentId,
			@RequestBody @Parameter(description = "Document object that contains a content field with updated content", required = true, example = "{\"content\":\"This is the updated sample document content\"}") Document document) {
		return documentService.updateDocument(document, documentId);
	}

	@RequestMapping(value = "{documentId}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(description = "Delete a document by document ID", summary = "Delete a document by document ID", security = {
			@SecurityRequirement(name = "privateAuthUser", scopes = {}) })
	@ApiResponses(value = { @ApiResponse(responseCode = "204", description = "No content (delete successful)"),
			@ApiResponse(responseCode = "404", description = "Not found") })
	public void deleteDocument(
			@PathVariable @Parameter(description = "The document ID that was returned in the document object from the POST command", required = true) String documentId) {
		documentService.deleteDocument(documentId);
	}
}
