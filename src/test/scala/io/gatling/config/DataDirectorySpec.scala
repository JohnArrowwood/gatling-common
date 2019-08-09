package io.gatling.config

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import org.arrowwood.gatling.common.UnitTest

class DataDirectorySpec extends UnitTest {
  lazy val feeder = csv( "test/foobar.csv" )
  def behavior : ScenarioBuilder = 
    scenario( "Testing ability to read from a feeder file" )
    .feed( feeder )
    .exec( session => 
      if ( session("foo").as[String] != "one" ||
           session("bar").as[String] != "apple" ) session.markAsFailed
      else session
    )
    .feed( feeder )
    .exec( session => 
      if ( session("foo").as[String] != "two" ||
           session("bar").as[String] != "banana" ) session.markAsFailed
      else session
    )
}
