package org.arrowwood.gatling.common.session

import io.gatling.commons.validation._
import io.gatling.core.Predef._
import io.gatling.core.session._

// define the environment variables that every scenario needs
// in order to make use of the debugging facility
trait CommonSessionVariables {

    implicit def variableToString( v: SessionVariable ) : String = v.toString

    def $( v : SessionVariable ) = v.value

    // session variables used for debugging
    case object REQUEST_NAME    extends SessionVariable
    case object REQUEST_METHOD  extends SessionVariable
    case object REQUEST_URI     extends SessionVariable
    case object RESPONSE_STATUS extends SessionVariable
    case object RESPONSE_BODY   extends SessionVariable
    case object REDIRECT_TO     extends SessionVariable

    // other useful things
    case object DATETIME extends SessionVariable
    case object RANDOM_GUID extends SessionVariable

    /**
     * initialize
     * method you can define in your SessionVariables object
     * to control custom initialization.  To be called in your
     * scenario as .exec( SessionVariables.initialize )
     */
    def initialize : ( Session => Validation[Session] ) = 
        session => defaultValues( session )

    /**
     * defaultValues
     * Walk through the session and make sure that for every parameter
     * defined in this class, a session variable is created, to avoid
     * trying to interact with empty variables
     */
    def defaultValues( session : Session ) : Validation[Session] = {
        var s = session
        this.getClass
            .getMethods
            .toList
            .filter( _.getParameterTypes.isEmpty )
            .foreach( prop => {
                val name = prop.getName
                s = s.set( name, name + "::NOT_INITIALIZED")
            })
        s.set( DATETIME, ISO_TIMESTAMP )
         .set( RANDOM_GUID, RandomGuid )
    }
}

object CommonSessionVariables 
extends CommonSessionVariables 
{
}