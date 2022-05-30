package com.intuit.gatling.common.helper

import io.gatling.core.session.Session
/**
 * Authorization Header
 */
trait AuthHeaders extends TestProperties {

  val basicHeader: Map[String, String] = Map(
    "intuit_offeringId" -> offeringId,
    "intuit_originatingip" -> originatingIp,
    "intuit_test" -> "true",
    "Content-Type" -> "application/json")

  protected def basicPrivateAuth: String = s"Intuit_IAM_Authentication intuit_appid=$appId,intuit_app_secret=$appSecret"

  protected def browserAuth: String = s"Intuit_IAM_Authentication intuit_apikey=$apiKey"

  protected def basicPrivateAuthPlus(session: Session): String = s"Intuit_IAM_Authentication intuit_appid=$appId,intuit_app_secret=$appSecret" + s""",intuit_token_type=IAM-Ticket,intuit_userid=${session("userAuthId").as[String]},intuit_token=${ session("userTicket").as[String]}"""

  protected def basicPrivateAuth_with_workforceAuthId_workforceTicket(session: Session): String = s"Intuit_IAM_Authentication intuit_appid=$appId,intuit_app_secret=$appSecret" + s""",intuit_token_type=IAM-Ticket,intuit_realmid=50000000,intuit_userid=${session("workforceAuthId").as[String]},intuit_token=${ session("workforceTicket").as[String]}"""

}