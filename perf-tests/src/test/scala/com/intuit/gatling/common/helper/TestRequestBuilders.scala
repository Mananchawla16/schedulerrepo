package com.intuit.gatling.common.helper

import io.gatling.core.Predef.{exec, feed}
import io.gatling.core.feeder.FeederBuilder
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.request.builder.HttpRequestBuilder

trait TestRequestBuilders {
  /** Http Request chain builder without data randomValue */
  protected def Request(requestBuilder: HttpRequestBuilder): ChainBuilder = {
    exec(requestBuilder)
  }
  /** Http Request chain builder with data randomValue */
  protected def Request(feeder: FeederBuilder)(requestBuilder: HttpRequestBuilder): ChainBuilder = {
    feed(feeder)
      .exec(requestBuilder)
  }
}