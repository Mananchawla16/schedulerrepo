package com.intuit.gatling.common.helper

import com.typesafe.config.{Config, ConfigFactory, ConfigParseOptions, ConfigSyntax}
import com.typesafe.config._
import java.util.{Base64, Properties}

object ConfigHelper {

  private val configParseOptions: ConfigParseOptions =
    ConfigParseOptions.defaults().setSyntax(ConfigSyntax.CONF).setAllowMissing(false)

  val COMMON_PROP_FILE = "common-test.properties"
  val PROP_FILE: String = System.getProperty("TESTENV", "prf") + "-test.properties"

  val conf: Config = ConfigFactory.parseResourcesAnySyntax(PROP_FILE, configParseOptions)
    .withFallback(ConfigFactory.parseResourcesAnySyntax(COMMON_PROP_FILE, configParseOptions))
    .withFallback(ConfigFactory.defaultOverrides())
    .resolve()

}