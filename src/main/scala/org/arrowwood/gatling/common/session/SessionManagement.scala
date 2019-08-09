package org.arrowwood.gatling.common.session

import com.typesafe.scalalogging.StrictLogging
import io.gatling.commons.validation._
import io.gatling.core.Predef._
import io.gatling.core.json.JsonParsers
import io.gatling.core.session.Expression
import io.gatling.core.structure.StructureBuilder
import org.arrowwood.gatling.common.session.CommonSessionVariables._

/**
 * SessionManagement
 * This class adds additional Gatling DSL to facilitate easy session variable 
 * setting
 *
 *  USAGE:
 *
 *  behavior( name )
 *  .set( VAR, VALUE )                           // value of expression is interpolated and stored in VAR
 *  .set( VAR, $(VAR2) )                         // contents of VAR2 are stored in VAR
 *  .set( VAR, VAR2.value )                      // contents of VAR2 are stored in VAR
 *  .set( VAR, LIST.random )                     // one of the elements of LIST are stored in VAR
 *  .set( VAR ).from( VAR2 )                     // contents of VAR2 are stored in VAR
 *  .set( VAR ).to( VALUE )                      // value of expression is interpolated and stored in VAR
 *  .set( VAR ).to( $(VAR2) )                    // contents of VAR2 are stored in VAR
 *  .set( VAR ).to.value( VALUE )                // value of expression is interpolated and stored in VAR
 *  .set( VAR ).to.value( $(VAR2) )              // contents of VAR2 are stored in VAR
 *  .set( VAR ).to.value.of( VAR2 )              // contents of VAR2 are stored in VAR
 *  .set( VAR ).to.value.of( PATH ).from( VAR2 ) // extract first matching JSON PATH from VAR2
 *  .set( VAR ).to.list.of( PATH ).from( VAR2 )  // extract *ALL* matching JSON PATH from VAR2
 *  .set( VAR ).to.first( SOME_LIST )            // extract the first element from SOME_LIST into VAR
 *  .set( VAR ).to.last( SOME_LIST )             // extract the last element into VAR
 *  .set( VAR ).to.random( SOME_LIST )           // extract any element at random
 *
 *  FUTURE:
 *
 *  .push( LIST, VALUE )
 *  .push( VALUE ).onto( LIST )
 *  .pop( LIST ).into( VAR )
 *  .set( VAR ).pop( LIST ) ?????
 */
object SessionManagement {

  // TODO: Ability to do this is broken in 3.2.
  //  Need to figure out the right way to do this in the latest Gatling
  /*
  implicit class SessionManagementExtensions[T <: StructureBuilder[T]]( val c : T ) {

    implicit val jsonParsers = JsonParsers()
    private val extractorFactory = new JsonPathExtractorFactory

    private def setVarToVal( dest: SessionVariable, value: Any ) =
      c.exec( session => session.set( dest, value ) )

    private def setVarToValOf( dest: SessionVariable, src: String ) =
      c.exec( session => session.set( dest, session.attributes(src) ) )

    private def setVarToExpr( dest: SessionVariable, value: Expression[Any] ) = 
      c.exec( session => value(session).map( v => session.set( dest, v ) ) )

    private def extractAll( path: String, json: String ) = {
      val extractor = newMultipleExtractor[String](path)
      extractor( jsonParsers.boon.parse( json ) ) match {
        case Success(x) => x.toVector
        case Failure(msg) => throw new Error( msg )
      }
    }

    private def setVarExtract( dest: SessionVariable, path: String, src: SessionVariable) =
      c.exec( session => session.set( dest, extractAll( path, session(src).as[String] ).head ) )
      
    private def setVarExtractAll( dest: SessionVariable, path: String, src: SessionVariable ) =
      c.exec( session => session.set( dest, extractAll( path, session(src).as[String] ) ) )

    private def setVarToRandom( dest: SessionVariable, src: SessionVariable ) =
      setVarToExpr( dest, "${" + src + ".random()}" )

    private def setVarToFirst( dest: SessionVariable, src: SessionVariable ) =
      c.exec( session => session.set( dest, session(src).as[Vector[Any]].head ) )

    private def setVarToLast( dest: SessionVariable, src: SessionVariable ) =
      c.exec( session => session.set( dest, session(src).as[Vector[Any]].last ) )

    trait _JSON_EXTRACTOR    { 
      def from( src: SessionVariable ) : T 
    }
    trait _SET_TO_LIST {
      def of( path: String ) : _JSON_EXTRACTOR
    }
    trait _SET_TO_VALUE {
      def apply( value: Expression[Any] ) : T
      def of( src: SessionVariable ) : T
      def of( path: String ) : _JSON_EXTRACTOR
    }
    trait _SET_TO {
      def apply ( value: Expression[Any] ) : T
      def value : _SET_TO_VALUE
      def list  : _SET_TO_LIST
      def first ( src: SessionVariable ) : T
      def last  ( src: SessionVariable ) : T
      def random( src: SessionVariable ) : T
    }
    trait _SET {
      def from( src: SessionVariable ) : T
      def to  : _SET_TO
    }

    def set( dest: SessionVariable, src: SessionVariable ) = setVarToValOf( dest, src )
    def set( dest: SessionVariable, value: Expression[Any] ) = setVarToExpr( dest, value )
    def set( dest: SessionVariable ) = new _SET {
      def from( src: SessionVariable ) : T = setVarToValOf( dest, src )
      def to = new _SET_TO {
        def apply( value: Expression[Any] ) : T = setVarToExpr( dest, value )
        def value = new _SET_TO_VALUE {
          def apply( value: Expression[Any] ) : T = setVarToExpr ( dest, value )
          def of( src: SessionVariable ) : T = setVarToValOf( dest, src )
          def of( path: String ) = new _JSON_EXTRACTOR {
            def from( src: SessionVariable ) : T = setVarExtract( dest, path, src )
          }
        }
        def list = new _SET_TO_LIST {
          def of( path: String ) = new _JSON_EXTRACTOR {
            def from( src: SessionVariable ) : T = setVarExtractAll( dest, path, src )
          }
        }
        def first ( src: SessionVariable ) : T = setVarToFirst ( dest, src )
        def last  ( src: SessionVariable ) : T = setVarToLast  ( dest, src )
        def random( src: SessionVariable ) : T = setVarToRandom( dest, src )
      }
    }

    trait _DEBUG {
      def out( value : Any ) : T
      def expr( value : Expression[Any] ) : T
      def valueOf( name : SessionVariable ) : T
    } 
    class consoleLogger extends _DEBUG {
      def out( x : Any ) : T =
        c.exec( session => {
          println( x.toString )
          session
        })
      def expr( value : Expression[Any] ) : T =
        c.exec( session => { 
          value(session).map( v => println( v.toString ) ) 
          session
        }) 
      def valueOf( name : SessionVariable ) : T =
        c.exec( session => { 
          println( session(name).as[String] )
          session
        })      
    }
    def console = new consoleLogger
    
    class debugLogger extends _DEBUG with StrictLogging {
      def out( x : Any ) : T =
        c.exec( session => {
          logger.debug( x.toString )
          session
        })
      def expr( value : Expression[Any] ) : T =
        c.exec( session => { 
          value(session).map( v => logger.debug( v.toString ) ) 
          session
        }) 
      def valueOf( name : SessionVariable ) : T =
        c.exec( session => { 
          logger.debug( session(name).as[String] )
          session
        })      
    }
    def debug = new debugLogger
  }
}

import org.arrowwood.gatling.common.session.SessionManagement._

object set {
  def apply( dest: SessionVariable, src: SessionVariable ) = exec().set( dest, src )
  def apply( dest: SessionVariable, value: Expression[Any] ) = exec().set( dest, value )
  def apply( dest: SessionVariable ) = exec().set( dest )
}

object console {
  def out( x : Any ) = exec().console.out( x )
  def expr( x : Expression[Any] ) = exec().console.expr( x )
  def valueOf( x : SessionVariable ) = exec().console.valueOf( x )
}

object debug {
  def out( x : Any ) = exec().debug.out( x )
  def expr( x : Expression[Any] ) = exec().debug.expr( x )
  def valueOf( x : SessionVariable ) = exec().debug.valueOf( x )
}


   */
}