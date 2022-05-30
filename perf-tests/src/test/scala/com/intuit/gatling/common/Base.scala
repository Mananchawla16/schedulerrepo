package com.intuit.gatling.common

import com.intuit.gatling.common.helper.{AuthHeaders, TestDataFiles, TestProperties, TestRequestBuilders}
import io.gatling.core.session.Session

/** Use this as base trait for all the classes
 * This trait extends many other configHelpers traits
 */
trait Base extends TestDataFiles with TestRequestBuilders with TestProperties with AuthHeaders {
  protected val AUTHORIZATION_HEADER = "Authorization"
  protected def getNativeAuthFromSession(session: Session): String = "Bearer " + session("bearerToken").as[String]
}