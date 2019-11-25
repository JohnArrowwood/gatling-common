package org.arrowwood.gatling.common.simulation

import io.gatling.core.Predef._
import io.gatling.core.structure.PopulationBuilder

import org.arrowwood.gatling.common.Test 

trait StandardSimulation 
extends Simulation 
with CustomProtocols
with CustomAssertions
with MaxDuration
{

    def users : List[PopulationBuilder]

    setUp( users )
        .protocols( http_config )
        .pauses(
            if ( Test.usePauses ) exponentialPauses
            else                  disabledPauses
        )
        .assertions( assertions )
        .maxDuration( max_duration )

}