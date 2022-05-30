package com.intuit.gatling.common.helper

import java.net.InetAddress
import java.util.UUID
import com.typesafe.config.{Config, ConfigFactory}

trait TestProperties {
  /**
   * Default configuration is set in common-test.properties
   * If it needs overrides this can be done by -D system param
   */

  val host: String = System.getProperty("fake.host", ConfigHelper.conf.getString("fake.host"))
  val appId: String = System.getProperty("fake.appId", ConfigHelper.conf.getString("fake.appId"))
  val appSecret: String = System.getProperty("fake.appSecret", IdpsConfigResolver.getProperty("fake.appSecret"))
  val apiKey: String = System.getProperty("fake.apiKey", ConfigHelper.conf.getString("fake.apiKey"))

  /** Properties that can be overriden by system properties */
  val accessHost: String = System.getProperty("accessHost", ConfigHelper.conf.getString("access.host"))
  val ticketClient_appId: String = System.getProperty("ticket.appId", ConfigHelper.conf.getString("ticket.appId"))
  val ticketClient_appSecret: String = System.getProperty("ticket.appSecret", IdpsConfigResolver.getProperty("ticket.appSecret"))

  /** txn tag for the load test requests. modify it as per need */
  var testIdentifier: String = System.getProperty("testTag", "fake-perftest")
  var generateUUIDString: String = UUID.randomUUID().toString
  var tidIdentifierString: String = "testTag" + "-"

  def generateTID(transactionName: String): String = {
    return tidIdentifierString + "-" + UUID.randomUUID().toString + "-" + transactionName
  }

  val offeringId: String = "Intuit.reliability.engineering.fakerclient"
  /** Originating ip setting to load generator */
  val localhost: InetAddress = InetAddress.getLocalHost
  val localIpAddress: String = localhost.getHostAddress
  val originatingIp: String = localIpAddress

  /** Session id set to UUID */
  val sessionId: String = UUID.randomUUID().toString

}