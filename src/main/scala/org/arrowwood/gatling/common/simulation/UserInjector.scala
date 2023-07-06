package org.arrowwood.gatling.common.simulation

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.core.structure.PopulationBuilder

import org.arrowwood.gatling.common.Test

trait UserInjector {
  type Injector = ( ScenarioBuilder ) => PopulationBuilder

  // DEFINE UTILITIES TO AID IN INTERACTING WITH THE TEST EXECUTION CONTEXT/CONFIG

  /**
    * If the test execution context specifies Test.min_users, and that value is greater than 0, use it
    * Otherwise, use the supplied value as the default
    */
  def min_users_or( n : Double ) : Double = if ( Test.min_users > 0 ) Test.min_users else n
  def min_users_or( n : Int )    : Int    = if ( Test.min_users > 0 ) Test.min_users else n

  /**
    * Calculate the number of users (either total or per second, it doesn't matter )
    * by referencing the execution context variables:  Test.users * Test.multiplier
    */
  def target_users : Int = ( Test.users * Test.multiplier ).toInt

  // DEFINE STANDARD INJECTION PROFILES BASED ON THE TEST EXECUTION CONTEXT

  /**
    * burst injector
    * All users arrive at the same time 
    * When everybody is finished, the test is over
    * Use when you need to determine how many is too many, e.g. measuring the thread pool size and connection limit
    */
  def burst( n : Integer ) : Injector = _.inject( atOnceUsers( n ) )
  def burst                : Injector = burst( target_users ) 

  /**
    * open ramp injector
    * Users are slowly added (linearly) until the number of users meets the target
    * Use a ramp test with an open injection model to determine the saturation point of the system
    */
  def openRamp( from : Double, to : Double ) : Injector =
      ( scn : ScenarioBuilder ) =>
          scn.inject(
              rampUsersPerSec( from ) to ( to ) during ( Test.rampUpTime )   randomized,
              constantUsersPerSec        ( to ) during ( Test.duration )     randomized,
              rampUsersPerSec( to   ) to ( 0  ) during ( Test.rampDownTime ) randomized
          )
  def openRamp( to : Double ) : Injector = openRamp( min_users_or( 1.0 / 3600.0 ), to )
  def openRamp                : Injector = openRamp( target_users )

  /**
    * When simulating production workloads, you often want to specify the workload in terms of transactions
    * during a time period.  The easiest to understand and communicate is per hour.
    * Use this injection profile to specify your traffic volume in terms of users injected per hour,
    * and allow the Test.multiplier to scale that up or down according to the needs of the test
    */
  def perHour( multiplier : Double, rate : Double ) : Injector = openRamp( ( rate * multiplier ) / 3600.0 )
  def perHour(                      rate : Double ) : Injector = perHour( Test.multiplier, rate )
  
  /**
    * If there is an upper limit to a workload, you can tell the injector to cap the growth at that limit
    * WARNING!  The growth rate follows the multiplier.  This means the ramp will not necessarily take the
    * full Test.rampUpTime to reach the limit.  For example:
    *  - Limit is 1,000 users per hour
    *  - 1x is 100 users per hour
    *  - requested ramp is 20x over 2 hours
    *  - 20x is 2,000 users
    *  - Limit is 1,000 users
    *  - Limit is reached half way through the ramp time
    *  - as opposed to ramping to 10x over 2 hours, it ramps to 10x over one hour (as requested)
    *    and sustains it over the remaining hour of the ramp duration
    */
  def perHourLimit( multiplier : Double, rate : Double, limit : Double ) : Injector = {
      val total = rate * multiplier
      if ( total < limit ) perHour( multiplier, rate )
      else {
          val ratio = total / limit
          val from = min_users_or( 1.0 / 3600.0 )
          val to = limit
          val short_ramp = FiniteDuration( (Test.rampUpTime.toSeconds * ratio).toLong, "seconds" )
          val extended_duration = Test.duration + Test.rampUpTime - short_ramp
          ( scn : ScenarioBuilder ) =>
              scn.inject(
                  rampUsersPerSec( from ) to ( to ) during ( short_ramp )   randomized,
                  constantUsersPerSec        ( to ) during ( extended_duration )     randomized,
                  rampUsersPerSec( to   ) to ( 0  ) during ( Test.rampDownTime ) randomized
              )
      }
  }
  def perHourLimit( rate : Double, limit: Double ) : Injector = perHourLimit( Test.multiplier, rate, limit ) 

  /**
    * closed ramp injector
    * Like the open ramp injector, except instead of the goal being to inject so many users per second,
    * here the goal is to have so many users active concurrently at a given moment in time, 
    * and if, when a user terminates, if that takes the active user count below the target, 
    * a new user is immediately injected.
    * Use a closed injection model to determine transaction throughput capabilities of the system
    * As the user counts increase, the throughput increases . . . to a point
    */
  def closedRamp( from : Int, to : Int ) : Injector = 
      ( scn : ScenarioBuilder ) =>
          scn.inject(
              rampConcurrentUsers( from ) to ( to ) during ( Test.rampUpTime ),
              constantConcurrentUsers        ( to ) during ( Test.duration ),
              rampConcurrentUsers( to   ) to (  0 ) during ( Test.rampDownTime )
          )
  def closedRamp( to : Int ) : Injector = closedRamp( min_users_or(1), to )    
  def closedRamp             : Injector = closedRamp( target_users )

  /**
    * Closed injection model stairstep ramp test
    * Inject so many users at a time over a short ramp up period,
    * then sustain that level for some period of time,
    * and repeat until you reach the target number of users
    * To keep the math clean, you should always set the difference between
    * GATLING_MIN_USERS and GATLING_USERS to be an even multiple of the 
    * GATLING_RAMP_UP_STEP.  If you don't, you will end up with too many users 
    * at the end of the ramp duration 
    */
  def closedStepRamp( from : Int, to : Int, step : Int ) : Injector =
    (scn : ScenarioBuilder ) => {
      assert( step > 0 ) // don't let the user do something stupid!
      val range = to - from // how many users are to be injected, in total
      val steps = Math.ceil( range.toDouble / step.toDouble ).toInt + 1 // how many steps to reach the target, counting the first step
      val perStepRampTime = Test.rampStepTime
      val totalRampTime = steps * perStepRampTime
      val totalSustainTime = Test.rampUpTime - totalRampTime
      val perStepTime = FiniteDuration( Math.ceil( totalSustainTime.toMillis.toDouble / steps.toDouble ).toLong, MILLISECONDS )
      scn.inject(
        incrementConcurrentUsers( step )
        .times( steps )
        .eachLevelLasting( perStepTime )
        .separatedByRampsLasting( perStepRampTime )
        .startingFrom( from )
      )
    }
  def closedStepRamp( to: Int, step: Int ) : Injector = closedStepRamp( min_users_or(1), to, step )
  def closedStepRamp(          step: Int ) : Injector = closedStepRamp( target_users, step )
  def closedStepRamp                       : Injector = closedStepRamp( Test.rampUpStep )
}