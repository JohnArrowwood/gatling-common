package org.arrowwood.gatling.common.simulation

/**
 * ClosedRampTest
 * Defines a scenario where there are supposed to be N concurrent users at all times 
 * (varying up and down during the ramp periods)
 * During a closed injection model test, when a user terminates, another is immediately injected to take its place.
 * If the user never terminates, then it simply runs until it is forcefully terminated at the end of the simulation
 */
trait ClosedRampTest extends SingleScenarioTest  {

    def profile = closedRamp

}
