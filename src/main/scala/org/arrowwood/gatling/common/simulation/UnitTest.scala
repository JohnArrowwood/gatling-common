package org.arrowwood.gatling.common

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder

trait UnitTest extends Simulation {
    def behavior : ScenarioBuilder

    setUp( behavior.inject( atOnceUsers(1) ) )
        .protocols( Default.httpConfig )
        .pauses(
            if ( Test.usePauses ) exponentialPauses
            else                  disabledPauses
        )
        .assertions(
            global.failedRequests.count.is(0)
        )
  
}
