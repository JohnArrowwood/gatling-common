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

    private def inSeconds( path : String ) : FiniteDuration = {
        val seconds : Double = config.getDouble( path )
        val nanoseconds : Double = seconds * 1000000000.0
        Duration( nanoseconds.toLong, "nanoseconds" )
    }

    /**
     * Test.environment
     * A string representing the target, e.g. LOCAL, DEV, QA, PERF, STG, PROD, etc.
     * Primarily intended to be used for loading environment-specific configuration files
     */
    val environment  : String   = config.getString( "test.environment" )

    /**
     * Test.min_users
     * When ramping, start at this number (default = 0)
     */
    val min_users    : Int      = config.getInt( "test.min_users" )
    
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
    val rampUpTime   : FiniteDuration = inSeconds( "test.rampUpTime" )

    /**
     * Test.rampUpStep
     * When doing a step-ramp, how many virtual users to add every step
     * Default value is 1.  A good suggested value is one for each server in the application cluster
     */
    val rampUpStep : Int = config.getInt( "test.rampUpStep" )
    
    /**
     * Test.rampSteptinme
     * When ramping up during a stairstep ramp, how long to take to rise from one level to the next
     * Default is 1 second, to avoid too fast of a jump.
     */
    val rampStepTime : FiniteDuration = inSeconds( "test.rampStepTime" )

    /**
     * Test.duration
     * How long to sustain the load once the target load level has been reached
     * Applies to RampTest and MultiClientTest, and
     * may be used with custom simulation definitions
     */
    val duration     : FiniteDuration = inSeconds( "test.duration" )

    /**
     * Test.rampDownTime
     * How long to take to ramp down the traffic at the end of the test
     * Only applies to MultiClientTest or custom simulations with arrival-based
     * injection profiles (vs. the RampTest, in which each test user lives for the
     * full duration)
     */
    val rampDownTime : FiniteDuration = inSeconds( "test.rampDownTime" )

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

    /**
     * Test.maxRps
     * If set to a non-zero value, add request throttling to the injection profile
     * All pre-defined simulation classes honor this.
     */
    val maxRps: Int = config.getInt("test.maxRps")

}
