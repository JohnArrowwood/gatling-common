package org.arrowwood.gatling.common.rest

import io.gatling.core.Predef._
import io.gatling.core.body.Body
import io.gatling.http.Predef._

import org.arrowwood.gatling.common.session.CommonSessionVariables._

/**
 * RESTfulService
 * Base trait for defining objects that abstracts a RESTful service endpoint
 * Use the specific sub-traits to define the methods of the API
 * so that you get automatic debug support
 */
trait RESTfulService 
{
    protected val description : String  // the description that Gatling will associate the request with for reporting
    protected def url : String          // the URL to request

    // add request headers
    protected var requestHeaders: Map[String,String] = Map()
    protected def header( key: String, value: String ) : this.type = {
        requestHeaders += ( key -> value )
        this
    }

    // add query parameters
    protected var queryParams: Map[String,Any] = Map()
    protected def queryParameter( key: String, value: Any ) : this.type = {
        queryParams += ( key -> value )
        this
    }  

    // add form parameters
    protected var formParams: Map[String,Any] = Map()
    protected def formParameter( key: String, value: Any ) : this.type = {
        formParams += ( key -> value )
        this
    }

}

/**
 * RESTfulGET
 * trait to define an object that represents a GET request against a RESTful API
 */
trait RESTfulGET extends RESTfulService {
    def gatlingRequest = 
        http( description )
            .get( url )
            .header( HttpHeaderNames.Accept, HttpHeaderValues.ApplicationJson )
            .headers( requestHeaders )
            .queryParamMap( queryParams )
            .check( currentLocation.transformOption( x => Some(description) ).saveAs( REQUEST_NAME ) )
            .check( currentLocation.transformOption( x => Some("GET") ).saveAs( REQUEST_METHOD ) )
            .check( currentLocation.saveAs( REQUEST_URI ) )
            .check( status.saveAs( RESPONSE_STATUS ) )
            .check( bodyString.saveAs( RESPONSE_BODY ) )
}

/**
 * RESTfulPUT
 * trait to define an object that represents a PUT request against a RESTful API
 */
trait RESTfulPUT extends RESTfulService {
    protected var json : Body
    protected def body : Body = json  // override to make custom body construction logic
    def gatlingRequest =
        http( description )
            .put( url )
            .header( HttpHeaderNames.ContentType, HttpHeaderValues.ApplicationJson )
            .header( HttpHeaderNames.Accept,      HttpHeaderValues.ApplicationJson )
            .headers( requestHeaders )
            .queryParamMap( queryParams )
            .body( body )
            .check( currentLocation.transformOption( x => Some(description) ).saveAs( REQUEST_NAME ) )
            .check( currentLocation.transformOption( x => Some("PUT") ).saveAs( REQUEST_METHOD ) )
            .check( currentLocation.saveAs( REQUEST_URI ) )
            .check( status.saveAs( RESPONSE_STATUS ) )
            .check( bodyString.saveAs( RESPONSE_BODY ) )
}

/**
 * RESTfulPOST
 * trait to define an object that represents a POST request against a RESTful API
 */
trait RESTfulPOST extends RESTfulService {
    protected var json : Body
    protected def body : Body = json  // override to make custom body construction logic
    def gatlingRequest =
        http( description )
            .post( url )
            .header( HttpHeaderNames.ContentType, HttpHeaderValues.ApplicationJson )
            .header( HttpHeaderNames.Accept,      HttpHeaderValues.ApplicationJson )
            .headers( requestHeaders )
            .queryParamMap( queryParams )
            .body( body )
            .check( currentLocation.transformOption( x => Some(description) ).saveAs( REQUEST_NAME ) )
            .check( currentLocation.transformOption( x => Some("POST") ).saveAs( REQUEST_METHOD ) )
            .check( currentLocation.saveAs( REQUEST_URI ) )
            .check( status.saveAs( RESPONSE_STATUS ) )
            .check( bodyString.saveAs( RESPONSE_BODY ) )
}

/**
 * RESTfulFormPOST
 * trait to define an object that represents a POST request against a RESTful API
 * Differs from RESTfulPOST in that it sends standard URL-encoded form data
 */
trait RESTfulFormPOST extends RESTfulService {
    def gatlingRequest =
        http( description )
            .post( url )
            .header( HttpHeaderNames.ContentType, HttpHeaderValues.ApplicationFormUrlEncoded )
            .headers( requestHeaders )
            .queryParamMap( queryParams )
            .formParamMap( formParams )
            .check( currentLocation.transformOption( x => Some(description) ).saveAs( REQUEST_NAME ) )
            .check( currentLocation.transformOption( x => Some("POST") ).saveAs( REQUEST_METHOD ) )
            .check( currentLocation.saveAs( REQUEST_URI ) )
            .check( status.saveAs( RESPONSE_STATUS ) )
            .check( bodyString.saveAs( RESPONSE_BODY ) )
}

/**
 * RESTfulDELETE
 * trait to define an object that represents a DELETE request against a RESTful API
 */
trait RESTfulDELETE extends RESTfulService {
    def gatlingRequest =
        http( description )
            .delete( url )
            .header( HttpHeaderNames.Accept, HttpHeaderValues.ApplicationJson )
            .headers( requestHeaders )
            .queryParamMap( queryParams )
            .check( currentLocation.transformOption( x => Some(description) ).saveAs( REQUEST_NAME ) )
            .check( currentLocation.transformOption( x => Some("DELETE") ).saveAs( REQUEST_METHOD ) )
            .check( currentLocation.saveAs( REQUEST_URI ) )
            .check( status.saveAs( RESPONSE_STATUS ) )
            .check( bodyString.saveAs( RESPONSE_BODY ) )
}

/**
 * RESTfulHEAD
 * trait to define an object that represents a HEAD request against a RESTful API
 */
trait RESTfulHEAD extends RESTfulService {
    def gatlingRequest =
        http( description )
            .head( url )
            .header( HttpHeaderNames.Accept, HttpHeaderValues.ApplicationJson )
            .headers( requestHeaders )
            .queryParamMap( queryParams )
            .check( currentLocation.transformOption( x => Some(description) ).saveAs( REQUEST_NAME ) )
            .check( currentLocation.transformOption( x => Some("HEAD") ).saveAs( REQUEST_METHOD ) )
            .check( currentLocation.saveAs( REQUEST_URI ) )
            .check( status.saveAs( RESPONSE_STATUS ) )
            .check( bodyString.saveAs( RESPONSE_BODY ) )
}

/**
 * IdSetter
 * RESTful APIs often have an ID field.  Add this trait to inherit the
 * ability to define and set that ID.
 */
trait IdSetter
{
    protected var id : String
    def id( s : String ) : this.type = { id = s; this }
}

/**
 * CustomJson
 * Add this trait to a PUT or POST method to enable the ability to
 * override the default constructed request body contents with a custom value
 */
trait CustomJson
{
    protected var json : Body
    def body( s : String ) : this.type = { json = StringBody( s ); this }
    def body( b : Body )   : this.type = { json = b;               this }
}
