package com.intuit.gatling.common.helper

/**
 * Retrieves and resolves application configurations
 */

import com.intuit.idps.IdpsClient
import com.intuit.idps.service.rest.IdpsProperties.PropertiesNames
import com.typesafe.config.{Config, ConfigFactory, ConfigValueFactory}
import java.util.{Base64, Properties}

/**
 * Retrieves and resolves application configurations
 */
object IdpsConfigResolver {
  private val IDPS_KEY_CONTENT_SYSTEM_PROPERTY_KEY = "sensitive.gatling.idps.secretKeyContentB64"
  private val idpsClient: IdpsClient = {
    val idpsConf: Config = ConfigHelper.conf.getConfig("idps")
    val props: Properties = new Properties()
    val idpsEndpoint = idpsConf.getString("endpoint")
    props.setProperty(PropertiesNames.ENDPOINT.getName, idpsEndpoint)

    // Checking for encoded api key first to enable running frontline simulations.
    val idpsKeyContentBase64 = System.getProperty(IDPS_KEY_CONTENT_SYSTEM_PROPERTY_KEY)
    if (idpsKeyContentBase64.isInstanceOf[String] && idpsKeyContentBase64.length > 1) {
      val apiKeyId = idpsConf.getString("apiKeyId")
      props.setProperty(PropertiesNames.API_KEY_ID.getName, apiKeyId)
      val idpsPemContent = new String(Base64.getDecoder.decode(idpsKeyContentBase64))
      props.setProperty(PropertiesNames.API_SECRET_KEY_CONTENT.getName, idpsPemContent)
    }
    else if (idpsConf.hasPath("policyId")) {
      props.setProperty(PropertiesNames.POLICY_ID.getName, idpsConf.getString("policyId"))
    }

    IdpsClient.Factory.newInstance(props)
  }
  private var conf: Config = ConfigHelper.conf
  println(conf.getString("fake.appSecret"))

  /**
   * Retrieves property from configs and IDPS
   *
   * @param key property to return
   * @return value
   */
  def getProperty(key: String): String = key match {
    case _: String if conf.getString(key).startsWith("{secret}idps:/") => getIdpsSecretAndUpdateConfig(key)
    case _: String => conf.getString(key)
    case _ => throw new IllegalArgumentException("Unrecognized idps property " + key)
  }

  private def getIdpsSecretAndUpdateConfig(key: String): String = {
    val idpsPath = conf.getString(key)
    val secret = idpsClient.getSecretLatest(idpsPath.substring(14)).getStringValue
    conf = conf.withValue(key, ConfigValueFactory.fromAnyRef(secret))
    secret
  }
}
