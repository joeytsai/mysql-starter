
lazy val mysqlProject = (project in file("."))
  .enablePlugins(JtSbtPlugin)
  .settings(
    name := "mysql-starter",
    buildInfoObject := "BuildInfoMysqlStarter"
  )

