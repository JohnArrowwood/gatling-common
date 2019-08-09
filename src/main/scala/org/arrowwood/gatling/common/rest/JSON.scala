package org.arrowwood.gatling.common.rest

import org.json4s._
import org.json4s.native.JsonMethods._

/**
 * JSON
 * Wrapper object for json4s - JSON for Scala - library
 * Used to pretty-print raw JSON for debug logging
 */
object JSON {
  def out( s : String ) = pretty(render(parse(s)))
}
