package com.github.jt.dao

import javax.sql.DataSource

import com.github.jt.config.DbConfig
import com.zaxxer.hikari.{HikariDataSource, HikariConfig}
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.MigrationInfoService
import scalikejdbc._

/**
  * Sets up Mysql data source from configuration file
  * Migrate Database with Flyway.
  * Start up Hikari Connection Pool
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

  private val dataSource = createDataSource()
  private val flyway = JtFlyway(dataSource)

  flyway.migrate()
  ConnectionPool.singleton(new DataSourceConnectionPool(dataSource))

  // Entrypoint for ScalikeJdbc
  def borrow(): DB = {
    val conn = ConnectionPool.borrow()
    val db = DB(conn)
    db.autoClose(true)
    db
  }

  // For runtime query / health check
  def flywayInfo(): MigrationInfoService = flyway.info()

}


/**
  * From: https://flywaydb.org/documentation/api/
  *
  * Flyway brings the largest benefits when integrated within an application.
  * By integrating Flyway you can ensure that the application and its database will always be compatible,
  * with no manual intervention required. Flyway checks the version of the database and applies
  * new migrations automatically before the rest of the application starts. This is important,
  * because the database must first be migrated to a state the rest of the code can work with.
  */
class JtFlyway(val dataSource: DataSource) {
  private val flyway = new Flyway()
  flyway.setDataSource(dataSource)
  def migrate(): Int = flyway.migrate()
  def info(): MigrationInfoService = flyway.info()
}

object JtFlyway {
  def apply(dataSource: DataSource): JtFlyway = new JtFlyway(dataSource)
}
