package org.arrowwood.gatling.common

/**
 * Headers
 * Class defines standard sets of headers to send when making various types of requests
 * Intent is to refer here rather than defining them individually, and only add custom
 * headers when that is the intent of the test case 
 */
object Headers {

    val KEEP_ALIVE             : Map[String, String] = Map( "Keep-Alive"       -> "115" )
    val XHR                    : Map[String, String] = Map( "X-Requested-With" -> "XMLHttpRequest" )
    val SENDING_FORM_DATA      : Map[String, String] = Map( "Content-Type"     -> "application/x-www-form-urlencoded" )
    val SENDING_JSON           : Map[String, String] = Map( "Content-Type"     -> "application/json;charset=UTF-8" )
    val EXPECTING_JSON         : Map[String, String] = Map( "Accept"           -> "application/json, */*" )
    val EXPECTING_TEXT         : Map[String, String] = Map( "Accept"           -> "text/plain, */*" )
    val EXPECTING_JSON_OR_TEXT : Map[String, String] = Map( "Accept"           -> "application/json, text/plain, */*" )

    val get     : Map[String, String] = KEEP_ALIVE
    val post    : Map[String, String] = KEEP_ALIVE ++        SENDING_JSON
    val formPost: Map[String, String] = KEEP_ALIVE ++        SENDING_FORM_DATA
    val ajax    : Map[String, String] = KEEP_ALIVE ++ XHR ++                 EXPECTING_JSON_OR_TEXT
    val ajaxSend: Map[String, String] = KEEP_ALIVE ++ XHR ++ SENDING_JSON ++ EXPECTING_JSON_OR_TEXT

}
