package org.arrowwood.gatling.common.simulation

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder

import org.arrowwood.gatling.common._

trait UnitTest extends SingleScenarioTest {
    
    def profile = burst(1)
    override def assertions = List(
        global.failedRequests.count.is(0)
    )

}
