package org.arrowwood.gatling.common.session

import io.gatling.commons.validation._
import io.gatling.core.Predef._
import io.gatling.core.session._
import org.arrowwood.gatling.common.rest.JSON
import org.arrowwood.gatling.common.session.CommonSessionVariables._

object Debug {

    /**
     * Debug.LastRequest
     * use .exec( Debug.LastRequest ) to log the results of the last request for debugging purposes
     */
    def LastRequest : ( Session => Validation[Session] ) = session => {
        println( "\n===========================================================================" )
        println( "DEBUG - Request: " + session( REQUEST_NAME ).as[String] )
        println( session( REQUEST_METHOD ).as[String] + " " + session( REQUEST_URI ).as[String] )
        println( "---------------------------------------------------------------------------" )
        println( "STATUS: " + session( RESPONSE_STATUS ).as[String] )
        println( JSON.out( session( RESPONSE_BODY ).as[String] ) )
        println( "===========================================================================" )
        session
    }

    /**
     * Debug.ValueOf
     * use .exec( Debug.ValueOf( VARIABLE_NAME ) ) to dump the contents of a session variable
     */
    def ValueOf( s : SessionVariable ) : ( Session => Validation[Session] ) = session => {
        println( "\n===========================================================================" )
        println( "DEBUG - Value of Session Variable:" + s )
        println( "---------------------------------------------------------------------------" )
        val v = session( s ).as[Any]
        println( v match {
            case s   : String   => s
            case seq : Seq[Any] => " - " + seq.mkString( ",\n - " )
            case x              => x.toString
        })
        println( "===========================================================================" )
        session
    }
    
}