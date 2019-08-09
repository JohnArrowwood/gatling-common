package org.arrowwood.gatling.common.session

import java.util.UUID

/**
 * RandomGuid
 * An object whose toString method generates a random GUID
 * This object exists to be put in the RANDOM_GUID session object
 * so that you can generate a random GUID by simply referencing
 * that session variable
 */
object RandomGuid {
  override def toString: String = UUID.randomUUID().toString
}
