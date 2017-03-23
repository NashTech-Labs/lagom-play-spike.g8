import de.johoop.cpd4sbt.CopyPasteDetector

organization in ThisBuild := "com.knoldus"

version in ThisBuild := "1.0-SNAPSHOT"

scalaVersion in ThisBuild := "2.11.8"

val macwire = "com.softwaremill.macwire" %% "macros" % "2.2.5" % "provided"
val scalaTest = "org.scalatest" %% "scalatest" % "3.0.1" % Test
val cassandraApi = "com.datastax.cassandra" % "cassandra-driver-extras" % "3.0.0"
val mockito = "org.mockito" % "mockito-all" % "1.10.19" % Test
val playJsonDerivedCodecs = "org.julienrf" %% "play-json-derived-codecs" % "3.3"

lazy val `play-lagom` = (project in file("."))
  .aggregate(`producer-api`, `producer-impl`, `consumer-api`,
    `consumer-impl`)


lazy val `producer-api` = (project in file("producer-api"))
  .enablePlugins(CopyPasteDetector)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi,
      playJsonDerivedCodecs
    )
  )

lazy val `producer-impl` = (project in file("producer-impl"))
  .enablePlugins(LagomScala,CopyPasteDetector)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslPersistenceCassandra,
      lagomScaladslTestKit,
      lagomScaladslKafkaBroker,
      lagomScaladslKafkaClient,
      lagomScaladslBroker,
      cassandraApi,
      macwire,
      scalaTest,
      playJsonDerivedCodecs
    )
  )
  .settings(lagomForkedTestSettings: _*)
  .dependsOn(`producer-api`)

lazy val `consumer-api` = (project in file("consumer-api"))
  .enablePlugins(CopyPasteDetector)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi,
      playJsonDerivedCodecs
    )
  )

lazy val `consumer-impl` = (project in file("consumer-impl"))
  .enablePlugins(LagomScala,CopyPasteDetector)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslPersistenceCassandra,
      lagomScaladslTestKit,
      lagomScaladslKafkaBroker,
      lagomScaladslKafkaClient,
      lagomScaladslBroker,
      cassandraApi,
      macwire,
      scalaTest,
      mockito,
      playJsonDerivedCodecs
    )
  )
  .settings(lagomForkedTestSettings: _*)
  .dependsOn(`consumer-api`, `producer-api`)


lazy val webGateway = (project in file("web-gateway"))
  .settings(commonSettings: _*)
  .enablePlugins(PlayScala && LagomPlay)
    .settings(

    libraryDependencies ++= Seq(
      lagomScaladslServer,
      macwire,
     "org.ocpsoft.prettytime" % "prettytime" % "3.2.7.Final",
      "org.webjars" % "foundation" % "6.2.3",
      "org.webjars" % "foundation-icon-fonts" % "d596a3cfb3",
      playJsonDerivedCodecs

    )
    )
  .dependsOn(`producer-api`, `consumer-api`)

def commonSettings: Seq[Setting[_]] = Seq(
)

coverageExcludedPackages in `consumer-impl` :=
"""sample.helloworldconsumer.impl.HelloConsumerLoader""".stripMargin

coverageExcludedPackages in `producer-impl` :=
"""sample.helloworld.impl.HelloLoader""".stripMargin
// End => scoverage excluded files configuration according to projects


