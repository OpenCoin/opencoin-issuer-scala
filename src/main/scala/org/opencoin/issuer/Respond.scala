package org.opencoin.issuer

import org.opencoin.core.token.{Blind,Coin}
import org.opencoin.core.util.Base64
import org.opencoin.core.util.Base64Serializer
import org.opencoin.core.util.Base64Deserializer
import org.opencoin.core.util.CustomJson._
import java.util.{NoSuchElementException => NoSuchElement}
import com.twitter.util.Future
import com.twitter.finagle.Service
import com.twitter.finagle.http.Version.Http11
import com.twitter.finagle.http.{Http, RichHttp, Request, Response}
import com.twitter.finagle.http.path._
import com.twitter.finagle.http.Status._
import org.jboss.netty.handler.codec.http.HttpMethod._
import org.jboss.netty.handler.codec.http.HttpHeaders.Names._
import org.jboss.netty.handler.codec.http.HttpHeaders.Values._
import com.codahale.jerkson.Json._
import org.eintr.loglady.Logging

import org.codehaus.jackson.map.module.SimpleModule
import org.codehaus.jackson.Version
import org.codehaus.jackson.map.SerializationConfig
import org.codehaus.jackson.map.DeserializationConfig
import org.codehaus.jackson.map.ObjectWriter
import org.codehaus.jackson.map.ObjectMapper


/**
 * The web service itself. See welcome.txt for further information.
**/
class Respond(methods: Methods, prefixPath: String) extends Service[Request, Response] with Logging {
/*
*/
  val basePath = Root / prefixPath
  def apply(request: Request) = {
    try {
	  request.method -> Path(request.path) match {
	    case GET -> Root => Future.value {
		  val source = scala.io.Source.fromFile("static/index.html", "UTF-8")
		  val data = source.mkString
		  source.close ()
		  log.debug("data: %s" format data)
		  Responses.html(data, acceptsGzip(request))
	    }
	    case GET -> `basePath` / "cdds" / "latest" => Future.value {
		  log.debug("GET -> %s/cdds/latest has been called." format basePath)
		  val data = CustomJson.generate(methods.getLatestCdd) //Generate JSON syntax from object
		  log.debug("data: %s" format data)
		  Responses.json(data, acceptsGzip(request))
	    }
	    case GET -> `basePath` / "cdds" / "serial" / serial => Future.value {
		  log.debug("GET -> %s/cdds/serial/ has been called." format basePath)
		  val data = CustomJson.generate(methods.getCdd(serial.toInt)) //Generate JSON syntax from object
		  log.debug("data: %s" format data)
		  Responses.json(data, acceptsGzip(request))
	    }
	    case GET -> `basePath` / "mintkeys" / "denomination" / denom => Future.value {
		  log.debug("GET -> %s/mintkeys/denomination/<denom.> has been called." format basePath)
		  val data = CustomJson.generate(methods.getMintKeyCertificates(denom.toInt)) //Generate JSON syntax from object
		  log.debug("data: %s" format data)
		  Responses.json(data, acceptsGzip(request))
	    }
	    case GET -> `basePath` / "mintkeys" / "id" / id => Future.value {
		  log.debug("GET -> %s/mintkeys/id/<id> has been called." format basePath)
		  val data = CustomJson.generate(methods.getMintKeyCertificate(Base64(id))) //Generate JSON syntax from object
		  log.debug("data: %s" format data)
		  Responses.json(data, acceptsGzip(request))
	    }
	    case GET -> `basePath` / "mintkeys" => Future.value {
		  log.debug("GET -> %s/mintkeys/ has been called." format basePath)
		  val data = CustomJson.generate(methods.getAllMintKeyCertificates) //Generate JSON syntax from object
		  log.debug("data: %s" format data)
		  Responses.json(data, acceptsGzip(request))
	    }
	    case POST -> `basePath` / "validate" => Future.value {
		  log.debug("POST -> %s/validate has been called." format basePath)
		  val content = request.contentString
		  log.debug("request: %s" format content)
		  val p = parse[List[Any]](content) //Parse JSON syntax to object
		  val data = CustomJson.generate(methods.validate(p.head.asInstanceOf[String], p.tail.asInstanceOf[List[Blind]])) //Generate JSON syntax from object
		  log.debug("data: %s" format data)
		  Responses.json(data, acceptsGzip(request))
	    }
	    case POST -> `basePath` / "renew" => Future.value {
		  log.debug("POST -> %s/renew has been called." format basePath)
		  val content = request.contentString
		  log.debug("request: %s" format content)
		  val p = parse[List[AnyRef]](content) //Parse JSON syntax to object
		  val (coins, blind) = p partition (_.isInstanceOf[Coin]) //TODO test!
		  val data = CustomJson.generate(methods.renew(coins.asInstanceOf[List[Coin]], blind.asInstanceOf[List[Blind]])) //Generate JSON syntax from object
		  log.debug("data: %s" format data)
		  Responses.json(data, acceptsGzip(request))
	    }
	    case POST -> `basePath` / "invalidate" => Future.value {
		  log.debug("POST -> %s/invalidate has been called." format basePath)
		  val content = request.contentString
		  log.debug("request: %s" format content)
		  val p = parse[List[Any]](content) //Parse JSON syntax to object
		  val data = CustomJson.generate(methods.invalidate(p.head.asInstanceOf[String], p.tail.asInstanceOf[List[Coin]])) //Generate JSON syntax from object
		  log.debug("data: %s" format data)
		  Responses.json(data, acceptsGzip(request))
	    }
	    case GET -> `basePath` / "resume" => Future.value {
		  log.debug("POST -> %s/resume has been called." format basePath)
		  val content = request.contentString
		  log.debug("request: %s" format content)
		  val p = parse[String](content) //Parse JSON syntax to object
		  val data = CustomJson.generate(methods.resume(p)) //Generate JSON syntax from object
		  log.debug("data: %s" format data)
		  Responses.json(data, acceptsGzip(request))
	    }
	    case GET -> `basePath` => Future.value {
		  val source = scala.io.Source.fromFile("static/gulden.html", "UTF-8")
		  val data = source.mkString
		  source.close ()
		  log.debug("data: %s" format data)
		  Responses.html(data, acceptsGzip(request))
	    }
	    case _ => {
		  log.debug("Error: URL not found: " + Path(request.path).toString)
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
