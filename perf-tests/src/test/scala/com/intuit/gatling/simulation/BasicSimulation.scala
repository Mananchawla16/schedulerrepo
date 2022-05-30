/*
 * Msaas perf gatling test code template
 *
 * All rights reserved.
 */

package com.intuit.gatling.simulation

import com.intuit.gatling.common.helper.HttpDefault
import com.intuit.gatling.requests.FakeDocs
import com.intuit.gatling.scenarios.CoreScenario
import io.gatling.core.Predef.{details, _}
import io.gatling.core.structure.PopulationBuilder

class BasicSimulation extends Simulation with SystemTestProperties with HttpDefault {

  val listOfScenarios: List[PopulationBuilder] = List(
    CoreScenario.FakeDocumentLifeCycle
      .inject(
        rampUsersPerSec(0) to testRampUpUserCount during testRampSeconds,
        constantUsersPerSec(testConcurrentUserRate) during testDurationSeconds
      )
  )

  val listOfAssertions = List(
    global.failedRequests.percent.lte(5),
    details(FakeDocs.GET_DOCUMENT).responseTime.percentile(99).lte(1000),
    details(FakeDocs.CREATE_DOCUMENT).responseTime.percentile(99).lte(2000),
    details(FakeDocs.UPDATE_DOCUMENT).responseTime.percentile(99).lte(3000)
  )

  setUp(listOfScenarios)
    .assertions(listOfAssertions)
    .protocols(httpProtocol)
}
