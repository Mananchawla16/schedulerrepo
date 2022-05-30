package com.intuit.gatling.scenarios

import com.intuit.gatling.common.Base
import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import com.intuit.gatling.common.identity.AccessTicket
import com.intuit.gatling.requests.FakeDocs

object CoreScenario extends Base{

  val FakeDocumentLifeCycle: ScenarioBuilder = scenario("FakeDocument_LifeCycle")
    .exec(AccessTicket.login_and_cacheTicket(user_csv_file))
    .doIf("#{userTicket.exists()}") {
        (
          exec(FakeDocs.getAllDocuments)
            exec(FakeDocs.createDocument).
            exec(FakeDocs.getDocumentById).
            exec(FakeDocs.updateDocument).
            exec(FakeDocs.deleteDocument)
        )
    }

}