package com.github.jt.config

import com.typesafe.config.Config

/**
 * https://github.com/brettwooldridge/HikariCP
 * https://dev.mysql.com/doc/connector-j/en/connector-j-reference-configuration-properties.html
 */
class DbConfig(config: Config) {
  private val pre = "db"
  JtConfig.checkValid(config, pre)

  val username = config.getString(s"$pre.mysql.username")
  val password = config.getString(s"$pre.mysql.password")
  val url = config.getString(s"$pre.mysql.url")
  val maxPoolSize = config.getInt(s"$pre.connection.pool.max.size")
  val debugLogging = config.getBoolean(s"$pre.debug.logging")
}

object DbConfig extends DbConfig(JtConfig.config)