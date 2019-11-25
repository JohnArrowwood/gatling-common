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
trait OpenRampTest extends SingleScenarioTest {

    def profile = openRamp

}
