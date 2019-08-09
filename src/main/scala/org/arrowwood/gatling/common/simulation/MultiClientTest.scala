package org.arrowwood.gatling.common.simulation

import io.gatling.core.Predef._
import io.gatling.core.structure.PopulationBuilder
import org.arrowwood.gatling.common._

trait MultiClientTest extends Simulation {
    /**
     * Actual test simulation should define clients as a list of the clients to model
     */
    def clients : List[PopulationBehavior]

    // translate the clients into the form needed for Gatling
    private def behavior: List[PopulationBuilder] =
        clients.flatMap { _.behavior( Test.multiplier ) }

    // execute the simulation
    setUp( behavior )
        .protocols( Default.httpConfig )
        .pauses(
            if ( Test.usePauses ) exponentialPauses
            else                  disabledPauses
        )

}