package org.arrowwood.gatling.common.simulation

import scala.concurrent.duration.FiniteDuration

import org.arrowwood.gatling.common.Test

trait MaxDuration {
    def max_duration : FiniteDuration = Test.rampUpTime + Test.duration + Test.rampDownTime + FiniteDuration(5, "minutes")
}