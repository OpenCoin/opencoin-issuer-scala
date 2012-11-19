package org.opencoin.issuer

import com.twitter.finagle.{Codec, CodecFactory, Service, SimpleFilter}
import com.twitter.finagle.http.Version.Http11
import com.twitter.finagle.http.{Http, RichHttp, Request}
import com.twitter.finagle.builder.{Server, ServerBuilder}
import java.net.InetSocketAddress
import org.eintr.loglady.Logging
import org.scalaquery.session.Database

object Issuer extends Logging{

  //TODO Add command line argumants. See http://github.com/scopt/scopt
  def main(args: Array[String]) {
	if(args.length > 0) {
		if(args(0) == "--admin") Wizard
	}
	else {
		val port = util.Properties.envOrElse("PORT", "8080").toInt // Required for Heroku deployment
		val db: Database = Database.forURL("jdbc:h2:mem:test1;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver")
		val methods = new Methods(db)
		val prefixPath = "gulden" // Allows to run several issuers vor different currencies along with each other each at a separate base path.

		if (methods.init) {
			val handleExceptions = new HandleExceptions
			val respond = new Respond(methods, prefixPath)
			
			// Compose the Filters and Service together:
			val service = handleExceptions andThen respond
			
			// Bind the service:
			val server: Server = ServerBuilder()
			  .codec(RichHttp[Request](Http()))
			  .bindTo(new InetSocketAddress(port))
			  .name("Opencoin Issuer")
			  .build(service)
			println("Server started on port: %s" format port)
		}
		else println("ERROR: Init failed.")
	  }
	}
}