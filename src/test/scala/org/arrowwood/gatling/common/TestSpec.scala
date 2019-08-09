package org.arrowwood.gatling.common

import scala.concurrent.duration.Duration

import org.scalatest.FreeSpec

class TestSpec extends FreeSpec {

  // Testing the default values as a way of assuring that it was loaded

  "environment = 'QA'"        in assert( Test.environment == "QA" )
  "numUsers = 1"              in assert( Test.users == 1 )
  "rampUpTime = one minute"   in assert( Test.rampUpTime == Duration( 60, "seconds" ) )
  "duration = five minutes"   in assert( Test.duration == Duration( 300, "seconds" ) )
  "rampDownTime = one minute" in assert( Test.rampDownTime == Duration( 60, "seconds" ) )
  "multiplier = 1.0"          in assert( Test.multiplier == 1.0 )
  "usePauses = false"         in assert( Test.usePauses == false )

}
