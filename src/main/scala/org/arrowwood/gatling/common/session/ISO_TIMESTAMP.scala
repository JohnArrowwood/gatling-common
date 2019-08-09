package org.arrowwood.gatling.common.session

import java.text.SimpleDateFormat
import java.util.Date

/**
 * ISO_TIMESTAMP
 * Object to be stored in the session which will generate a timestamp
 * every time it is accessed.  Also adds ability to generate date objects
 * that are relative to now, like secondsAgo() or minutesFromNow()
 */
object ISO_TIMESTAMP {
    val isoFormat = "yyyy-MM-dd'T'HH:mm:ssZ"
    def format( d: Date ): String = new SimpleDateFormat( isoFormat ).format( d )

    def daysToMillis( x: Int ): Long    = x * 24 * 60 * 60 * 1000
    def hoursToMillis( x: Int ): Long   = x * 60 * 60 * 1000
    def minutesToMillis( x: Int ): Long = x * 60 * 1000
    def secondsToMillis( x: Int ): Long = x * 1000

    def daysAgo( num: Int ): String    = past( daysToMillis( num ) )
    def hoursAgo( num: Int ): String   = past( hoursToMillis( num ) )
    def minutesAgo( num: Int ): String = past( minutesToMillis( num ) )
    def secondsAgo( num: Int ): String = past( secondsToMillis( num ) )

    def daysFromNow( num: Int ): String    = future( daysToMillis( num ) )
    def hoursFromNow( num: Int ): String   = future( hoursToMillis( num ) )
    def minutesFromNow( num: Int ): String = future( minutesToMillis( num ) )
    def secondsFromNow( num: Int ): String = future( minutesToMillis( num ) )

    override def toString: String      = format( new Date() )
    def past( millis: Long ): String   = format( new Date( System.currentTimeMillis() - millis ) )
    def future( millis: Long ): String = format( new Date( System.currentTimeMillis() + millis ) )
}

