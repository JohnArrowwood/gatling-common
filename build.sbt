name := "gatling-common"

organization := "org.arrowwood"

version := "0.9.12"

//publishTo := Some(Resolver.file("file", new File("../gatling-common-repository")))
publishTo := sonatypePublishToBundle.value

enablePlugins(GatlingPlugin)
scalaVersion := "2.13.10"
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
    "io.gatling.highcharts" % "gatling-charts-highcharts" % "3.9.5",
    "io.gatling"            % "gatling-test-framework"    % "3.9.5",

    // ScalaTest
    "org.scalactic" %% "scalactic" % "3.0.8",
    "org.scalatest" %% "scalatest" % "3.0.8" % "test",

    // TypeSafe Config
    "com.typesafe" % "config" % "1.3.4",

    // JSON for Scala
    "org.json4s" %% "json4s-native" % "3.6.7"
)

credentials += Credentials(
  "GnuPG Key ID",
  "gpg",
  "4770F2904AE3DB8BAAF395522804B53DD3E95CC5",
  "ignored"
)

Global / useGpgPinentry := false
