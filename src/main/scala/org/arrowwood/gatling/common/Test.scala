package org.arrowwood.gatling.common

import com.typesafe.config.ConfigFactory

import scala.concurrent.duration.{Duration, FiniteDuration}

/**
 * Test
 * Object that exposes the configured test execution properties
 * These properties can be tweaked from the command-line by setting environment 
 * variables prior to scenario execution.  
 */
object Test {
 
    private val config = ConfigFactory.load( "test-parameters" )

    /**
     * Test.environment
     * A string representing the target, e.g. LOCAL, DEV, QA, PERF, STG, PROD, etc.
     * Primarily intended to be used for loading environment-specific configuration files
     */
    val environment  : String   = config.getString( "test.environment" )

    /**
     * Test.users
     * How many users to ramp up to.
     * Only applies to the RampTest simulation, or
     * may be used with custom simulation definitions
     */
    val users        : Int      = config.getInt( "test.users" )

    /**
     * Test.rampUpTime
     * How long to take to ramp up to the target load level
     * Applies to RampTest and MultiClientTest, and
     * may be used with custom simulation definitions
     */
    val rampUpTime   : FiniteDuration = Duration( config.getLong( "test.rampUpTime" ), "seconds" )

    /**
     * Test.duration
     * How long to sustain the load once the target load level has been reached
     * Applies to RampTest and MultiClientTest, and
     * may be used with custom simulation definitions
     */
    val duration     : FiniteDuration = Duration( config.getLong( "test.duration" ), "seconds" )

    /**
     * Test.rampDownTime
     * How long to take to ramp down the traffic at the end of the test
     * Only applies to MultiClientTest or custom simulations with arrival-based
     * injection profiles (vs. the RampTest, in which each test user lives for the
     * full duration)
     */
    val rampDownTime : FiniteDuration = Duration( config.getLong( "test.rampDownTime" ), "seconds" )

    /**
     * Test.multiplier
     * Built-in scaling of defined traffic levels
     * MultiClientTest uses this to let you scale traffic up or down to some multiple
     * of the defined user arrival rate, e.g. 1x, 2x, 0.1x, etc.
     */
    val multiplier   : Double   = config.getDouble( "test.multiplier" )

    /**
     * Test.usePauses
     * Whether or not to honor pauses, or skip them, during test execution
     * All pre-defined simulation classes honor this.
     * Only takes effect if the scenario defines pauses.
     */
    val usePauses    : Boolean  = config.getBoolean( "test.usePauses" )

}
