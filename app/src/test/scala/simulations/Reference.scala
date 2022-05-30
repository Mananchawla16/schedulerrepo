package simulations

import com.intuit.karate.gatling.PreDef._
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._
import scala.concurrent.duration._
import scala.language.postfixOps

import com.intuit.ctg.perf._

class Reference extends Simulation {

   var startTime: Long = System.currentTimeMillis
    before {
      startTime = System.currentTimeMillis
    }

  val protocol = karateProtocol()

  val reference = scenario("reference").exec(karateFeature("classpath:karate/document/reference.feature"))

  setUp(
    reference.inject(rampUsers(5) during (5 seconds))
  ).protocols(protocol)
    .assertions(
        details("GET /v1/documents").responseTime.percentile4.lt(SLA.CAPABILITY_COMPONENT_TP99_RESPONSE_TIME),
        details("GET /v1/documents").failedRequests.percent.lt(100 - SLA.GLOBAL_AVAILABILITY_RATE),
        details("POST /v1/documents").responseTime.percentile1.lt(SLA.CAPABILITY_COMPONENT_TP50_RESPONSE_TIME),
        details("POST /v1/documents").responseTime.percentile3.lt(SLA.CAPABILITY_COMPONENT_TP95_RESPONSE_TIME),
        details("POST /v1/documents").responseTime.percentile4.lt(SLA.CAPABILITY_COMPONENT_TP99_RESPONSE_TIME),
        details("POST /v1/documents").failedRequests.percent.lt(100 - SLA.GLOBAL_AVAILABILITY_RATE),
      )

  // Not sure how Score card method sends data about the identification of the underlying service in the first place.
  after {
    val endTime = System.currentTimeMillis
    ScoreCard.writeResults(pass = true, startTime, endTime)
  }
}