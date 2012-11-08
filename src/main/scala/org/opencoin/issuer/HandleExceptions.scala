package org.opencoin.issuer

import com.twitter.finagle.{Service, SimpleFilter}
import com.twitter.finagle.http.{Request, Response}
import com.twitter.finagle.http.Status._
import org.eintr.loglady.Logging

/**
 * A simple Filter that catches exceptions and converts them to appropriate
 * HTTP responses.
 */

class HandleExceptions extends SimpleFilter[Request, Response] with Logging {
  def apply(request: Request, service: Service[Request, Response]) = {
    // `handle` asynchronously handles exceptions.
    service(request) handle { case error =>
      log.debug("HandleException called.")
      val statusCode = error match {
        case _: IllegalArgumentException =>
          Forbidden
        case _ =>
          InternalServerError
      }
      Responses error error.getMessage
    }
  }
}
