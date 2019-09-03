package org.arrowwood.gatling.common.simulation

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import org.arrowwood.gatling.common.{Default, _}


/**
 * RampTest
 * Defines a scenario where the behavior is an infinite loop
 * It ramps the user count up to the target number of concurrent users
 * and then sustains it for the duration
 * finally killing the virtual users when the end is reached
 */
trait RampTest extends Simulation {

    // the behavior to be modeled 
    def behavior : ScenarioBuilder

    setUp( 
        behavior
            .inject(
                rampUsers( Test.users ) during ( Test.rampUpTime ),
                nothingFor( Test.duration )
            )
    )
    .protocols( Default.httpConfig )
    .pauses( 
        if ( Test.usePauses ) exponentialPauses
        else                  disabledPauses
    )
    .maxDuration( Test.rampUpTime + Test.duration )

}
