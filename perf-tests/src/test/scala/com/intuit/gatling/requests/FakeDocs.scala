package com.intuit.gatling.requests

import com.intuit.gatling.common.Base
import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._

object FakeDocs extends Base {

  val GET_ALL_DOCUMENTS = "get_all_documents"
  val GET_DOCUMENT = "get_document"
  val CREATE_DOCUMENT = "create_document"
  val UPDATE_DOCUMENT = "update_document"
  val DELETE_DOCUMENT = "delete_document"

  def getAllDocuments(): ChainBuilder = Request {
    http(GET_ALL_DOCUMENTS)
      .get("/v1/documents")
      .header("intuit_tid",session=>generateTID("fakeDocLoadTest"))
      .header("Authorization", basicPrivateAuthPlus)
      .check(status.is(200))
  }

  def createDocument(): ChainBuilder = Request {
    http(CREATE_DOCUMENT)
      .post("/v1/documents")
      .header("intuit_tid",session=>generateTID("fakeDocLoadTest"))
      .header("Authorization", basicPrivateAuthPlus)
      .body(StringBody("""{"content":"This is the a new sample document content"}"""))
      .check(jsonPath("$.id").saveAs("documentId"))
      .check(status.is(200))
  }

  def getDocumentById(): ChainBuilder = Request {
    http(GET_DOCUMENT)
      .get("/v1/documents/#{documentId}")
      .header("intuit_tid",session=>generateTID("fakeDocLoadTest"))
      .header("Authorization", basicPrivateAuthPlus)
      .check(jsonPath("$.id").saveAs("documentId"))
      .check(status.is(200))
  }

  def updateDocument(): ChainBuilder = Request {
    http(UPDATE_DOCUMENT)
      .put("/v1/documents/#{documentId}")
      .header("intuit_tid",session=>generateTID("fakeDocLoadTest"))
      .header("Authorization", basicPrivateAuthPlus)
      .body(StringBody("""{"content":"This is an updated sample document content"}"""))
      .check(jsonPath("$.id").saveAs("documentId"))
      .check(status.is(200))
  }

  def deleteDocument(): ChainBuilder = Request {
    http(DELETE_DOCUMENT)
      .delete("/v1/documents/#{documentId}")
      .header("intuit_tid",session=>generateTID("fakeDocLoadTest"))
      .header("Authorization", basicPrivateAuthPlus)
      .check(status.is(204))
  }
}
