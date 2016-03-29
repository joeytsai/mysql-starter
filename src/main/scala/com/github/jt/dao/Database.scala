package com.github.jt.dao

import javax.sql.DataSource

import com.github.jt.config.DbConfig
import com.zaxxer.hikari.{HikariDataSource, HikariConfig}
import scalikejdbc._

/**
 * Created by joeyt on 3/29/16.
 */
object Database {

  private val DataSourceClassName = "com.mysql.jdbc.jdbc2.optional.MysqlDataSource"

  def createDataSource(): DataSource = {
    val config = new HikariConfig()
    config.setDataSourceClassName(DataSourceClassName)
    config.addDataSourceProperty("url", DbConfig.url)
    config.setUsername(DbConfig.username)
    config.setPassword(DbConfig.password)

    config.setMaximumPoolSize(DbConfig.maxPoolSize)
    // maxLifetime:
    // We strongly recommend setting this value, and it should be at least 30 seconds less than any database-level connection timeout.
    // *Think* the MySQL connection timeout this is referring to is WAIT_TIMEOUT, which defaults to 28800 (8 hours)

    GlobalSettings.loggingSQLAndTime = LoggingSQLAndTimeSettings(enabled = DbConfig.debugLogging)

    new HikariDataSource(config)
  }

  def borrow(): DB = {
    val conn = ConnectionPool.borrow()
    val db = DB(conn)
    db.autoClose(true)
    db
  }

  ConnectionPool.singleton(new DataSourceConnectionPool(createDataSource()))
}
