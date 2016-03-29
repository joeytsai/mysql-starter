import com.github.jt.sbt.Deps
import com.github.jt.sbt.Deps._

lazy val deps = Seq(
  Deps.config,
  Database.mysql,
  Database.pool,
  Database.jdbc
)

lazy val mysqlProject = (project in file("."))
  .enablePlugins(JtSbtPlugin)
  .settings(
    name := "mysql-starter",
    buildInfoObject := "BuildInfoMysqlStarter",
    libraryDependencies ++= deps
  )

