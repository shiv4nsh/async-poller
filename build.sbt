name := "async-poller"

version := "0.1"

scalaVersion := "2.12.2"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http" % "10.0.11",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0",
  "org.json4s" %% "json4s-native" % "3.6.0-M2",
  "de.heikoseeberger" %% "akka-http-json4s" % "1.20.0",
  "com.typesafe.akka" %% "akka-testkit" % "2.5.12" % Test,
  "org.scalatest" %% "scalatest" % "3.0.0" % Test,
  "org.mockito" % "mockito-core" % "2.18.3" % Test
)
