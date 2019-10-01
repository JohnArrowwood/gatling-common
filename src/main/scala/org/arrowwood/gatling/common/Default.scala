package org.arrowwood.gatling.common

import io.gatling.core.Predef._
import io.gatling.http.Predef._

object Default {

    /**
     * Default.httpConfig
     * Definition of the default request object/parameters, 
     * so that they can be defined once and referenced as needed
     * TODO: CUSTOMIZE THIS
     */ 
    val httpConfig =
        http
            .acceptCharsetHeader("ISO-8859-1,utf-8;q=0.7,*;q=0.7")
            .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
            .acceptEncodingHeader("gzip, deflate")
            .acceptLanguageHeader("en-us;q=0.5,en;q=0.3,*;q=0.1")
            .disableCaching
            .disableFollowRedirect

}