package org.arrowwood.gatling.common.simulation

import io.gatling.core.structure.ScenarioBuilder

/**
 * UserBehavior
 * Define the behavior of a single user, or flow.
 * Primarily used to make the intent of the code self-documenting.
 * Also adds a level of insulation against type changes inside Gatling
 * if the type of object returned by simulation() were to ever change
 */
trait UserBehavior {
    def behavior: ScenarioBuilder
}