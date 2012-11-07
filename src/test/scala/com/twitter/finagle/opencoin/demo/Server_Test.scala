import org.scalatest.FlatSpec
import scala.collection.mutable.Stack
import com.twitter.finagle.opencoin.demo.Server

import java.net.InetSocketAddress
import com.twitter.finagle.Codec
import com.twitter.finagle.CodecFactory
import com.twitter.finagle.Service
import com.twitter.finagle.builder.ClientBuilder
import com.twitter.finagle.builder.Server
import com.twitter.finagle.builder.ServerBuilder
import com.twitter.util.Future
import org.jboss.netty.channel.ChannelPipelineFactory
import org.jboss.netty.channel.Channels
import org.jboss.netty.handler.codec.frame.DelimiterBasedFrameDecoder
import org.jboss.netty.handler.codec.frame.Delimiters
import org.jboss.netty.handler.codec.string.StringDecoder
import org.jboss.netty.handler.codec.string.StringEncoder
import org.jboss.netty.util.CharsetUtil
import java.net.NetworkInterface
import scala.collection.JavaConversions._
import java.lang.Exception
//import org.squeryl.PrimitiveTypeMode._
import org.squeryl.Schema
import org.squeryl.SessionFactory
import org.squeryl.Session
import org.squeryl.adapters.MySQLAdapter
import org.jboss.netty.buffer.ChannelBuffers.copiedBuffer
import org.jboss.netty.handler.codec.http.{HttpRequest, HttpResponse, DefaultHttpResponse, QueryStringDecoder}
import org.jboss.netty.handler.codec.http.HttpVersion.HTTP_1_1
import org.jboss.netty.handler.codec.http.HttpResponseStatus.OK
import org.jboss.netty.util.CharsetUtil.UTF_8
import com.twitter.finagle.http.Http
import org.jboss.netty.handler.codec.http._
import com.codahale.jerkson.Json._


class StackSpec extends FlatSpec {
	print("Database username: ")
    var databaseUsername = readLine
    print("Database password: ")
    var databasePassword = readLine
    val databaseConnection = "jdbc:mysql://localhost:3306/Open_Coin"
    Class.forName("com.mysql.jdbc.Driver")
    SessionFactory.concreteFactory = Some(() => Session.create(
		java.sql.DriverManager.getConnection(databaseConnection, databaseUsername, databasePassword),
		new MySQLAdapter)
	      )
   Server.connection(databaseConnection,databaseUsername,databasePassword)
   /*
    * Test case test response function with GET method. The input value is coin's type which exist in database. 
    * Assume: client will send request {"type":"USD"}. Server will receive client request.
    * Then, it will find coin which match type is inputed.
    * When coin is found, server will send to client this coin.
    */
   "Response function with GET method" must "return a coin which match type is inputed" in {
        var method: String = "GET" /* Test response function with GET method */
	    var request_str: String = """{"type":"USD"}""" /* Request is {"type":"USD"} */
	    var ccoin = Server.get_coin("USD") /* Get coin from database.*/
	    var res: String = generate(ccoin) /* Translate Jerkson string. This is expected result. */
	    /*
	     * Compare returned value of response function with expected result. If they are equal, test case pass
	     */
    	assert(Server.Response(method, request_str).toString() === res)
    }
	/*
    * Test case test response function with GET method. The input value is coin's type which don't exist in database. 
    * Assume: client will send request {"type":"URO"}. Server will receive client request.
    * Then, it will find coin which match type is inputed.
    * When coin is not found, server will send to client a string "URO type is not found"
    */
	it should "return coin's type is not found" in {
		var method: String = "GET" /* Test response function with GET method */
	    var request_str: String = """{"type":"URO"}""" /* Request is {"type":"URO"} */
	    /*
	     * Compare returned value of response function with string: "URO is not found". If they are equal, test case pass
	     */
    	assert(Server.Response(method, request_str).toString() === "URO is not found!" )
	}
	/*
    * Test case test response function with POST method. The input value is coin's type which exist in database. 
    * Assume: client will send request {"type":"USD","value":1500}. Server will receive client request.
    * It will find coin which match type is inputed.
    * If coin is found, server will sum value which value inputed. Then, server update coin into database and get coin to send to client.
    */
	"Response function with POST method" must "sum coin's value" in {
        var method: String = "POST" /* Test response function with GET method */
	    var request_str: String = """{"type":"USD","value":1500}""" /* Request is {"type":"USD"} */
	    var ccoin = Server.get_coin("USD") /* Get coin from database.*/
	    ccoin.Value = ccoin.Value + 1500
	    var res: String = generate(ccoin) /* Translate Jerkson string. This is expected result. */
	    /*
	     * Compare returned value of response function with expected result. If they are equal, test case pass
	     */
    	assert(Server.Response(method, request_str).toString() === res)
    }
	/*
    * Test case test response function with POST method. The input value is coin's type which don't exist in database. 
    * Assume: client will send request {"type":"USD","value":1500}. Server will receive client request.
    * Then, it will find coin which match type is inputed.
    * When coin is not found, server will send to client a string "URO type is not found"
    */
	it should "coin's type is not found" in {
		var method: String = "POST"
	    var request_str: String = """{"type":"URO","value":1500}""" 
    	assert(Server.Response(method, request_str).toString() === "URO is not found!" )
	}
	/*
	 * Test case test response function with other method. Server will send to client a string "The method doesn't support"
	 */
	"Response function with other method" must "return message this method doesn't support" in {
        var method: String = "PUT" /* Test response function with GET method */
	    var request_str: String = """{"type":"USD","value":1500}""" /* Request is {"type":"USD"} */
	    var ccoin = Server.get_coin("USD") /* Get coin from database.*/
	    ccoin.Value = ccoin.Value + 1500
	    var res: String = method + " doesn't support" /* Translate Jerkson string. This is expected result. */
	    /*
	     * Compare returned value of response function with expected result. If they are equal, test case pass
	     */
    	assert(Server.Response(method, request_str).toString() === res)
    }
}
