package org.arrowwood.gatling.common.simulation

import org.arrowwood.gatling.common.Test

/**
 * BurstTest
 * Test injecting some number of users all at once, and measuring how the system responds and/or recovers
 */
trait BurstTest extends SingleScenarioTest {
    
    def profile = burst( ( Test.users * Test.multiplier ).toInt )
  
}
