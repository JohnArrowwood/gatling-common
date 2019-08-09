package org.arrowwood.gatling.common.session

import org.arrowwood.gatling.common.session.CommonSessionVariables._
import org.scalatest.FreeSpec

class SessionVariableSpec extends FreeSpec {

  "FOO.toString = FOO"             in assert( REQUEST_URI.toString() == "REQUEST_URI" )
  "FOO.value = '${FOO}'"           in assert( REQUEST_URI.value      == "${REQUEST_URI}" )
  "$(FOO) = FOO.value = '${FOO}'"  in assert( $(REQUEST_URI)         == "${REQUEST_URI}" )
  "FOO.first = '${FOO(0)}'"        in assert( REQUEST_URI.first      == "${REQUEST_URI(0)}" )
  "FOO.size = '${FOO.size()}'"     in assert( REQUEST_URI.size       == "${REQUEST_URI.size()}" )
  "FOO.random = '${FOO.random()}'" in assert( REQUEST_URI.random     == "${REQUEST_URI.random()}" )

}
