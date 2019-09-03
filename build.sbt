name := "gatling-common"

organization := "org.arrowwood"

version := "0.3"

publishTo := Some(Resolver.file("file", new File("../gatling-common-repository")))

enablePlugins(GatlingPlugin)
scalaVersion := "2.12.0"
scalacOptions := Seq(
    "-encoding","UTF-8",
    "-target:jvm-1.8",
    "-deprecation",
    "-feature",
    "-unchecked",
    "-language:implicitConversions",
    "-language:postfixOps"
)
libraryDependencies ++= Seq(

    // Gatling
    "io.gatling.highcharts" % "gatling-charts-highcharts" % "3.2.0",
    "io.gatling"            % "gatling-test-framework"    % "3.2.0",

    // ScalaTest
    "org.scalactic" %% "scalactic" % "3.0.8",
    "org.scalatest" %% "scalatest" % "3.0.8" % "test",

    // TypeSafe Config
    "com.typesafe" % "config" % "1.3.4",

    // Scala Logging
    "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",

    // JSON for Scala
    "org.json4s" %% "json4s-native" % "3.6.7"
)
