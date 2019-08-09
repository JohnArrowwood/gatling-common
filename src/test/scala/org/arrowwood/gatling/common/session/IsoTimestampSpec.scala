package org.arrowwood.gatling.common.session

import java.text.SimpleDateFormat
import java.util.Date

import org.scalatest.FreeSpec

class IsoTimestampSpec extends FreeSpec {

  val pattern = """^\d\d\d\d-\d\d-\d\dT\d\d:\d\d:\d\d[-+]\d\d\d\d$"""
  def dateOf( d: Date ) = new SimpleDateFormat( "yyyy-MM-dd" ).format( d )

  ".toString() produces an ISO 8601 compatible string of the current date/time" in
    assert( ISO_TIMESTAMP.toString().matches(pattern) )

  ".daysToMillis(1) = 86,400,000 milliseconds" in 
    assert( ISO_TIMESTAMP.daysToMillis(1) == 86400000 )

  // WARNING: The next two tests may fail if run too close to midnight GMT
  // (date changes between calculation of actual and expected)

  ".daysAgo(1) = yesterday" in {
    val actual = ISO_TIMESTAMP.daysAgo(1)
    val expectedDate = new Date( System.currentTimeMillis() - 84400000 )
    val expected = dateOf( expectedDate )
    assert( actual.startsWith( expected ) )
  }

  ".daysFromNow(1) = tomorrow" in {
    val actual = ISO_TIMESTAMP.daysFromNow(1)
    val expectedDate = new Date( System.currentTimeMillis() + 84400000 )
    val expected = dateOf( expectedDate )
    assert( actual.startsWith( expected ) )
  }


}
