import Dependencies._

name := "graphql-scala"

version := "0.1"

scalaVersion := "2.12.8"

libraryDependencies ++= Seq(
  "org.sangria-graphql" %% "sangria" % Version.sangriaGQL,
  "org.sangria-graphql" %% "sangria-spray-json" % Version.sangriaSprayJson,

  "com.typesafe.akka" %% "akka-http" % Version.akkaHttp,
  "com.typesafe.akka" %% "akka-http-spray-json" % Version.akkaHttp,
  "com.typesafe.akka" %% "akka-stream" % Version.akkaStream,

  "com.h2database" % "h2" % Version.h2,
  "com.typesafe.slick" %% "slick" % Version.slick,
  "com.typesafe.slick" %% "slick-hikaricp" % Version.slick,
  "org.slf4j" % "slf4j-nop" % "1.6.6",

  "org.scalatest" %% "scalatest" % "3.0.4" % Test
)