import Dependencies._

name := "graphql-scala"

version := "0.1"

scalaVersion := "2.12.8"

libraryDependencies ++= Seq(
  "org.sangria-graphql" %% "sangria" % Version.sangriaGQL,
  "org.sangria-graphql" %% "sangria-spray-json" % Version.sangriaSprayJson,

  "ch.qos.logback" % "logback-classic" % Version.classicLogging,
  "com.typesafe.scala-logging" %% "scala-logging" % Version.scalaLogging,

  "com.typesafe.akka" %% "akka-http" % Version.akkaHttp,
  "com.typesafe.akka" %% "akka-stream" % Version.akkaStream
)