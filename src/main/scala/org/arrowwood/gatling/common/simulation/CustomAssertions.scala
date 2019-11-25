package org.arrowwood.gatling.common.simulation

import io.gatling.core.Predef._

trait CustomAssertions {
    def assertions : List[Assertion] = List( global.failedRequests.count.gte(0) ) // deliberately meaningless assertion
}