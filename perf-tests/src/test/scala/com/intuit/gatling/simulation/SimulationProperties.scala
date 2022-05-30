package com.intuit.gatling.simulation

import com.intuit.gatling.common.helper.ConfigHelper


/**
 *  Test properties to be overriden in gatling, frontline, pipeline as -D params
 */

trait SystemTestProperties {

  protected val testRampSeconds: Int =System.getProperty("rampUpSeconds", ConfigHelper.conf.getString("rampUpSeconds")).toInt
  protected val testRampUpUserCount: Int =System.getProperty("rampUpUserCount", ConfigHelper.conf.getString("rampUpUserCount")).toInt
  protected val testConcurrentUserRate: Int =System.getProperty("concurrentUserPerSec", ConfigHelper.conf.getString("concurrentUserPerSec")).toInt
  protected val testDurationSeconds: Int = System.getProperty("durationSeconds", ConfigHelper.conf.getString("durationSeconds")).toInt
  protected val debug: Boolean = System.getProperty("debug", "true").toBoolean
  protected val availabilityPerc: Float = System.getProperty("availabilityPerc", "99.00").toFloat
  protected val minUserCount: Int = 1

}