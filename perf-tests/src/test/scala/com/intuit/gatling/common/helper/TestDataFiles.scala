package com.intuit.gatling.common.helper

import com.intuit.gatling.scenarios.CoreScenario.user_csv_file
import io.gatling.core.Predef.{configuration, csv}

trait TestDataFiles {
  val supportedEnv = Array("perf", "prod")
  val env = System.getProperty("env", "perf")
  if (!supportedEnv.contains(env)) {
    System.err.println("Unsupported environment property!" + env)
    System.exit(1)
  }
  val user_csv_file = csv(s"data/${env}/users.csv").circular
}
