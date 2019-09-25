package org.arrowwood.gatling.common.simulation

import io.gatling.core.Predef._
import io.gatling.core.structure.{PopulationBuilder, ScenarioBuilder}
import org.arrowwood.gatling.common.Test

/**
 * PopulationBehavior
 * When modeling system behavior as a series of individual flows
 * You use UserBehavior to define the flow of a single user
 *
 * To control the arrival rates of users, and to group together
 * different users doing different things, put them all together
 * in a PopulationBehavior
 *
 * Intended to be used to group related users, such as those from
 * a particular client application.  There is nothing to limit you
 * to that usage, however.
 */
trait PopulationBehavior {

    /**
     * perHourLimit
     * For client behavior that should not exceed some limit, 
     * no matter what the multiplier says
     */
    def perHourLimit( 
        multiplier: Double,
        rate: Double, 
        limit: Double, 
        flow: ScenarioBuilder 
    ): PopulationBuilder =
        if ( rate * multiplier < limit ) perHour( multiplier, rate,  flow )
        else                             perHour(        1.0, limit, flow )

    def perHour( 
        multiplier: Double,
        rate: Double,
        flow: ScenarioBuilder
    ): PopulationBuilder = {
        val from = if ( Test.min_users > 0 ) Test.min_users else 1.0 / 3600.0
        val to = ( rate * multiplier ) / 3600.0
        flow.inject( 
            rampUsersPerSec( from ) to ( to ) during ( Test.rampUpTime )   randomized,
            constantUsersPerSec        ( to ) during ( Test.duration )     randomized,
            rampUsersPerSec( to   ) to ( 0  ) during ( Test.rampDownTime ) randomized
        )
    }

    // the main simulation calls this method to get the list of 
    // scenarios to be executed for this population of users
    def behavior( x : Double ) : List[PopulationBuilder]
    
}
