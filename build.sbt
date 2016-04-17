import com.github.jt.sbt.Deps

lazy val deps = Deps.databaseDeps ++ Seq(
  "org.flywaydb" % "flyway-core" % "4.0"
)

lazy val mysqlProject = (project in file("."))
  .enablePlugins(JtSbtPlugin)
  .settings(
    name := "mysql-starter",
    buildInfoObject := "BuildInfoMysqlStarter",
    libraryDependencies ++= deps,

    // would be nice if we could get the flyway values from DbConfig
    flywayUrl := "jdbc:mysql://localhost:3306/dbname"
  )

