package org.arrowwood.gatling.common.simulation

import io.gatling.core.Predef._
import io.gatling.core.structure.PopulationBuilder
import org.arrowwood.gatling.common._

trait MultiScenarioTest extends Simulation {
    /**
     * Actual test simulation should define scenarios as a list of the scenarios to model
     * Scenarios are open injection models (no loops)
     * Scenarios are defined inside objects that extend PopulationBehavior
     * Objects define 'behavior' as the Gatling scenario
     * and define 'behavior(x)' to apply a 'production-level' injection model
     * times the multiplier (x), using the built-in 'perHour()' function
     */
    def scenarios : List[PopulationBehavior]

    // translate the scenario into the form needed for Gatling
    private def behavior: List[PopulationBuilder] =
        scenarios.flatMap { _.behavior( Test.multiplier ) }

    // execute the simulation
    setUp( behavior )
        .protocols( Default.httpConfig )
        .pauses(
            if ( Test.usePauses ) exponentialPauses
            else                  disabledPauses
        )
    .maxDuration( Test.rampUpTime + Test.duration + Test.rampDownTime + 300 )

}