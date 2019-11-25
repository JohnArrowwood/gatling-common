package org.arrowwood.gatling.common.simulation

import io.gatling.core.structure.PopulationBuilder

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
trait PopulationBehavior extends UserInjector {

    // the main simulation calls this method to get the list of 
    // scenarios to be executed for this population of users
    def behavior( x : Double ) : List[PopulationBuilder]
    
}
