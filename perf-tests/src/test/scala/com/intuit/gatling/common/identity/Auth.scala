package com.intuit.gatling.common.identity

import com.intuit.gatling.common.Base
import com.intuit.gatling.common.helper.{TicketCache, TicketInfo}
import io.gatling.core.Predef._
import io.gatling.core.feeder.FeederBuilder
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._

import scala.concurrent.duration._
import scala.language.postfixOps

object AccessTicket extends Base {

  val LABEL_ACCESS_SIGNIN="accessSign"
  val LABEL_ACCESS_EXCHANGE_CBT = "accessExchangeCBT"

  val ticketClientAuthHeader  = s"Intuit_IAM_Authentication intuit_appid=$ticketClient_appId,intuit_app_secret=$ticketClient_appSecret"
  /**
   * Access Call - signIn - get Opaque ticket
   */
  def accessSignIn(datatypeBuilder: FeederBuilder): ChainBuilder = Request(datatypeBuilder){
    http(LABEL_ACCESS_SIGNIN)
      .post(accessHost+"/v2/web/tickets/sign_in")
      .header("Authorization", ticketClientAuthHeader)
      .headers(basicHeader)
      .header("intuit_tid",session=>generateTID("accessSignIn"))
      .body(StringBody("""{"username":"#{userName}","password":"#{password}"}"""))
      .check(jsonPath("$..iamTicket.ticket").saveAs("userTicket"))
      .check(jsonPath("$..iamTicket.userId").saveAs("userAuthId"))
      .check(jsonPath("$..iamTicket.agentId").saveAs("agentId"))
      .check(status.is(200),regex("iamTicket"))
  }

  /**
   *  Function for making the tickets gets created and stored in cache.
   *  This ensure access calls for tickets doesn't happen all the time, but instead gets created and stored in cache on load generators
   *  The number of calls to tickets would be equal to the number of records in the data file for unique name/password. So the calls to access should stop once tickets are cached
   * Logic used:
   * 1. initiate the lookupCache for the tickets
   * 2. Feed the data file to the login function to make sure this function is configurable
   * 3. Check if the userName field is used for the "first" time. If yes, then makes ticket call and stores ticket Info in map
   */

  private val ticketCache_forAllUsers = new TicketCache()
  private val exchangeForCbt : Boolean = System.getProperty("login.ticket.exchangeForCBT", "false").toBoolean

  def login_and_cacheTicket(dataFileCsv:FeederBuilder) = {
    println("in here")
    feed(dataFileCsv) // Feed the data file to the login function to make sure this function is configurable
      .doIfOrElse(session => ticketCache_forAllUsers.isValidTicketAvailable("#{userName}", false)) {
        exec(session => {
          println(session)
          val ticket = ticketCache_forAllUsers.getTicket(session("userName").as[String])
          session
            .set("userTicket", ticket.ticketId)
            .set("userAuthId", ticket.authId)
        })
      } {
        exec(signIn())
          .doIf(exchangeForCbt) {
            exec(accessExchangeCBT())
          }
          .doIf("#{userTicket.exists()}") {
            exec(session => {
              ticketCache_forAllUsers.cacheTicket(session("userName").as[String], TicketInfo (session("userTicket").as[String], session("userAuthId").as[String], session("agentId").as[String], System.currentTimeMillis())) // Put into ticket cache userName,userTicket,userAuthId,agentId
              session
            })
          }
          .pause(0 seconds, 1 seconds)
      }
  }

  def signIn(): ChainBuilder = Request{
    http(LABEL_ACCESS_SIGNIN)  // Do access login
      .post(accessHost + "/v2/web/tickets/sign_in")
      .headers(basicHeader)
      .header("Authorization", ticketClientAuthHeader)
      .header("intuit_tid",session=>generateTID("accessSignIn"))
      .body(StringBody("""{"username":"#{userName}","password":"#{password}"}"""))
      .check(
        status.is(200),regex("iamTicket"),
        jsonPath("$.iamTicket.ticket").saveAs("userTicket"),  // save ticket,authId,agentId(same as authId)
        jsonPath("$.iamTicket.userId").saveAs("userAuthId"),
        jsonPath("$.iamTicket.userId").saveAs("agentId"))
  }

  /**
   * Access sign In and get exchange CBT
   */
  def accessExchangeCBT(): ChainBuilder = Request{
    http(LABEL_ACCESS_EXCHANGE_CBT)
      .post(accessHost+"/v2/tickets/exchange_cbt")
      .header("Authorization", ticketClientAuthHeader)
      .headers(basicHeader)
      .header("intuit_tid",session=>generateTID("accessExchangeCBT"))
      .body(StringBody(session => s"""{"agentId":"${session("agentId").as[String]}"}"""))
      .check(jsonPath("$.ticket").saveAs("userTicket"))
      .check(jsonPath("$.userId").saveAs("userAuthId"))
      .check(jsonPath("$.agentId").saveAs("agentId"))
      .check(status.is(200))
  }

}