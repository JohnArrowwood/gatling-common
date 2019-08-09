package org.arrowwood.gatling.common.session

import org.scalatest.FreeSpec

class RandomGuidSpec extends FreeSpec {

  "RandomGuid.toString() should produce an all-lowercase GUID" in 
    assert( RandomGuid.toString().matches("^[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}$") )

}
