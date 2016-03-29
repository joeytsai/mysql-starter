package com.github.jt.config

import java.util.Map.Entry

import com.typesafe.config.{ConfigValue, Config, ConfigFactory}

/**
 * Created by joeyt on 3/29/16.
 */
object JtConfig {
  private val appConf = ConfigFactory.load()
  private val refConf = ConfigFactory.defaultReference()
  val config = appConf.withFallback(refConf).resolve()

  def checkValid(config: Config, pathPrefix: String): Unit = {
    config.checkValid(refConf, pathPrefix)
    val configKeys = getConfigKeys(config, pathPrefix)
    val refKeys = getConfigKeys(refConf, pathPrefix)
    val keysNotInRef = configKeys.diff(refKeys)
    if (keysNotInRef.nonEmpty) throw new IllegalArgumentException(s"Unknown config keys: ${keysNotInRef.mkString(", ")}")
  }

  private def getConfigKeys(config: Config, pathPrefix: String): Set[String] = {
    val es = config.entrySet()
    val kvArray: Array[Entry[String, ConfigValue]] = es.toArray(new Array(es.size))
    kvArray.map(_.getKey).filter(_.startsWith(pathPrefix)).toSet
  }

}
