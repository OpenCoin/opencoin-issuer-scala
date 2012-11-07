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

  /**
   * The web service itself. See welcome.txt for further information.
   */
class Respond(issuer: Issuer, prefixPath: String) extends Service[Request, Response] with Logging {
  val basePath = Root / prefixPath

  def apply(request: Request) = {
    try {
	  request.method -> Path(request.path) match {
	    case GET -> Root => Future.value {
		  //val data = "Welcome to the opencoin issuer."
		  //val data = scala.io.Source.fromFile("welcome.txt").getLines
		  val source = scala.io.Source.fromFile("welcome.txt")
		  val data = source.mkString
		  source.close ()
		  log.debug("data: %s" format data)
		  Responses.text(data, acceptsGzip(request))
	    }
	    case GET -> `basePath` / "cdds" / "latest" => Future.value {
		  log.debug("GET -> %s/cdds/latest has been called." format basePath)
		  val data = generate(issuer.getLatestCdd) //Generate JSON syntax from object
		  log.debug("data: %s" format data)
		  Responses.json(data, acceptsGzip(request))
	    }
	    case GET -> `basePath` / "cdds" / "serial" / serial => Future.value {
		  log.debug("GET -> %s/cdds/serial/ has been called." format basePath)
		  val data = generate(issuer.getCdd(serial.toInt)) //Generate JSON syntax from object
		  log.debug("data: %s" format data)
		  Responses.json(data, acceptsGzip(request))
	    }
	    case GET -> `basePath` / "mintkeys" / "denomination" / denom => Future.value {
		  log.debug("GET -> %s/mintkeys/denomination/<denom.> has been called." format basePath)
		  val data = generate(issuer.getMintKeyCertificates(denom.toInt)) //Generate JSON syntax from object
		  log.debug("data: %s" format data)
		  Responses.json(data, acceptsGzip(request))
	    }
	    case GET -> `basePath` / "mintkeys" / "id" / id => Future.value {
		  log.debug("GET -> %s/mintkeys/id/<id> has been called." format basePath)
		  val data = generate(issuer.getMintKeyCertificate(Base64(id))) //Generate JSON syntax from object
		  log.debug("data: %s" format data)
		  Responses.json(data, acceptsGzip(request))
	    }
	    case POST -> `basePath` / "validate" => Future.value {
		  log.debug("POST -> %s/validate has been called." format basePath)
		  val content = request.contentString
		  log.debug("request: %s" format content)
		  val p = parse[List[Any]](content) //Parse JSON syntax to object
		  val data = generate(issuer.validate(p.head.asInstanceOf[String], p.tail.asInstanceOf[List[Blind]])) //Generate JSON syntax from object
		  log.debug("data: %s" format data)
		  Responses.json(data, acceptsGzip(request))
	    }
	    case POST -> `basePath` / "renew" => Future.value {
		  log.debug("POST -> %s/renew has been called." format basePath)
		  val content = request.contentString
		  log.debug("request: %s" format content)
		  val p = parse[List[AnyRef]](content) //Parse JSON syntax to object
		  val (coins, blind) = p partition (_.isInstanceOf[Coin]) //TODO test!
		  val data = generate(issuer.renew(coins.asInstanceOf[List[Coin]], blind.asInstanceOf[List[Blind]])) //Generate JSON syntax from object
		  log.debug("data: %s" format data)
		  Responses.json(data, acceptsGzip(request))
	    }
	    case POST -> `basePath` / "invalidate" => Future.value {
		  log.debug("POST -> %s/invalidate has been called." format basePath)
		  val content = request.contentString
		  log.debug("request: %s" format content)
		  val p = parse[List[Any]](content) //Parse JSON syntax to object
		  val data = generate(issuer.invalidate(p.head.asInstanceOf[String], p.tail.asInstanceOf[List[Coin]])) //Generate JSON syntax from object
		  log.debug("data: %s" format data)
		  Responses.json(data, acceptsGzip(request))
	    }
	    case GET -> `basePath` / "resume" => Future.value {
		  log.debug("POST -> %s/resume has been called." format basePath)
		  val content = request.contentString
		  log.debug("request: %s" format content)
		  val p = parse[String](content) //Parse JSON syntax to object
		  val data = generate(issuer.resume(p)) //Generate JSON syntax from object
		  log.debug("data: %s" format data)
		  Responses.json(data, acceptsGzip(request))
	    }
	    case _ => {
		  log.debug("Error: URL not found.")
		  Future value Responses.notFoundError("Error: URL not found.", acceptsGzip(request))
	    }
	  }
    } catch {
	  case e: NoSuchElement => Future value Response(Http11, NotFound)
	  case e: Exception => Future.value {
	    val message = Option(e.getMessage) getOrElse "Something went wrong."
	    sys.error("Error Message: %s\nStack trace:\n%s"
		  .format(message, e.getStackTraceString))
	    Responses.error(message, acceptsGzip(request))
	  }
    }
  }

  def acceptsGzip(request: Request) =
    if (request.containsHeader(ACCEPT_ENCODING))
	  request.getHeader(ACCEPT_ENCODING).split(",").contains(GZIP)
    else false
}