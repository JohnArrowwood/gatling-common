package org.arrowwood.gatling.common.simulation

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder

import org.arrowwood.gatling.common._

/**
 * OpenRampTest
 * Defines a scenario where the behavior is a flow of requests (no loop)
 * It ramps the injection rate up to the target 
 * and then sustains it for the duration
 * and ramps down at the end
 * finally killing any virtual users still running 5 minutes after
 * the end of the ramp-down period
 */
trait OpenRampTest extends Simulation {

    // the behavior to be modeled 
    def behavior : ScenarioBuilder

    // the ramp parameters
    val from = 1.0 / 3600.0
    val to = Test.users * Test.multiplier // per second

    setUp( 
        behavior
            .inject(
                rampUsersPerSec( from ) to ( to ) during ( Test.rampUpTime )   randomized,
                constantUsersPerSec        ( to ) during ( Test.duration )     randomized,
                rampUsersPerSec( to   ) to ( 0  ) during ( Test.rampDownTime ) randomized
            )
    )
    .protocols( Default.httpConfig )
    .pauses( 
        if ( Test.usePauses ) exponentialPauses
        else                  disabledPauses
    )
    .maxDuration( Test.rampUpTime + Test.duration + Test.rampDownTime + 300 )

}
