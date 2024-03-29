package org.arrowwood.gatling.common.simulation

import io.gatling.core.structure.PopulationBuilder

trait SingleScenarioTest 
extends StandardSimulation 
with UserBehavior
with UserInjector
{
    def profile : Injector
    def users : List[PopulationBuilder] = List( profile( behavior ) )
}