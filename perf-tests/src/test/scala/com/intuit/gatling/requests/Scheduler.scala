package com.intuit.gatling.requests

import com.intuit.gatling.common.Base
import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._

object Scheduler extends Base {

  val CREATE_SCHEDULE = "create_scheduler"

  def createSchedule(): ChainBuilder = Request {
    http(CREATE_SCHEDULE)
      .post("v1/jobs/schedule-oinp-job")
      .header("intuit_tid",session=>generateTID("schedulerLoadTest"))
      .header("Authorization", basicPrivateAuthPlus)
      .body(StringBody("""{"scheduleId":"test-schedule","triggerAt":"2022-06-15T23:30:00+01:00","triggerData": "{\"authId\":\"ghfhgj\",\"sourceObjectId\":\"ghhg\",\"sourceServiceId\":\"fhg\",\"creationDate\":\"gfhj\"}"}"""))
      .check(jsonPath("$.id").saveAs("scheduleApiId"))
      .check(status.is(200))
  }
