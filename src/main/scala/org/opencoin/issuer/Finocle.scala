package org.opencoin.issuer

import org.opencoin.core.token.{Blind,Coin}
import org.opencoin.core.util.Base64
import java.net.InetSocketAddress
import java.util.{NoSuchElementException => NoSuchElement}
import java.lang.Exception
import com.twitter.util.Future
import com.twitter.finagle.{Codec, CodecFactory, Service, SimpleFilter}
import com.twitter.finagle.http.Version.Http11
import com.twitter.finagle.http.{Http, RichHttp, Request, Response}
import com.twitter.finagle.http.path._
import com.twitter.finagle.http.Status._
import com.twitter.finagle.builder.{ClientBuilder, Server, ServerBuilder}
import org.jboss.netty.handler.codec.http.HttpMethod._
import org.jboss.netty.channel.ChannelPipelineFactory
import org.jboss.netty.channel.Channels
import org.jboss.netty.handler.codec.frame.DelimiterBasedFrameDecoder
import org.jboss.netty.handler.codec.frame.Delimiters
import org.jboss.netty.handler.codec.string.StringDecoder
import org.jboss.netty.handler.codec.string.StringEncoder
import org.jboss.netty.handler.codec.http.HttpHeaders.Names._
import org.jboss.netty.handler.codec.http.HttpHeaders.Values._
import org.jboss.netty.util.CharsetUtil
import org.jboss.netty.util.CharsetUtil.UTF_8
import com.codahale.jerkson.Json._
import scala.collection.JavaConversions._
import org.eintr.loglady.Logging
import org.scalaquery.session.Database

object Finocle extends Logging{
  
  //TODO Add command line argumants. See http://github.com/scopt/scopt
  def main(args: Array[String]) {
    val db: Database = Database.forURL("jdbc:h2:mem:test1;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver")
    val issuer = new Issuer(db)
	val port = 8080
	val host = "localhost"
	val prefixPath = "api"

	if (issuer.init) {
		val handleExceptions = new HandleExceptions
		val respond = new Respond(issuer, prefixPath)
		
		// Compose the Filters and Service together:
		val service = handleExceptions andThen respond
		
		// Bind the service:
		val server: Server = ServerBuilder()
		  .codec(RichHttp[Request](Http()))
		  .bindTo(new InetSocketAddress(host, port))
		  .name("Finocle")
		  .build(service)
		println("Server started on port: %s" format port)
	}
	else println("ERROR: Init failed.")
  }

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

}