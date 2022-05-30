package com.intuit.gatling.common.helper

import com.intuit.gatling.common.Base
import io.gatling.core.Predef._
import io.gatling.http.Predef.http
import io.gatling.http.protocol.HttpProtocolBuilder

/**
 * Define any common helper functions
 */
trait HttpDefault extends Base {

  val httpProtocol: HttpProtocolBuilder = http
    .baseUrl(host)
    .inferHtmlResources()
    .acceptHeader("application/json")
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("en-US")
    .contentTypeHeader("application/json; charset=UTF-8")
    .userAgentHeader("gatling-frontline")
    .connectionHeader("Keep-Alive")
    .disableWarmUp
    .shareConnections
    .disableCaching
}
