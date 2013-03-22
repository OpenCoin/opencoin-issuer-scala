package org.opencoin.issuer

import org.jboss.netty.handler.codec.http.HttpHeaders.Names._
import org.jboss.netty.handler.codec.http.HttpHeaders.Values._
import org.jboss.netty.handler.codec.http.HttpResponseStatus._
import org.jboss.netty.buffer.ChannelBuffers.copiedBuffer
import org.jboss.netty.util.CharsetUtil.UTF_8
import com.twitter.finagle.http.Response
import org.jboss.netty.handler.codec.http.HttpResponseStatus
import com.twitter.finagle.http.Status._
import com.twitter.finagle.http.Version.Http11
import java.io.ByteArrayOutputStream
import java.util.zip.GZIPOutputStream
import org.eintr.loglady.Logging

object Responses extends Logging {
  def json(data: String, gzip: Boolean = false) = {
  	log.debug("Responses built, json, data: %s" format data)
    val response = Response()
    response.setContentTypeJson
    response.setHeader("Access-Control-Allow-Origin", "*") //TODO Assess whether this is a risk or okay.
    if (gzip) response.setHeader(CONTENT_ENCODING, GZIP)
    response.content = content(data, gzip)
    response
  }

  def notFoundError(info: String, gzip: Boolean = false) = {
  	log.debug("Responses built, error, data: %s" format info)
    val response = Response(Http11, NotFound)
    response.mediaType = "text/plain"
    if (gzip) response.setHeader(CONTENT_ENCODING, GZIP)
    response.content = content(info, gzip)
    response
  }
  
  def error(info: String, gzip: Boolean = false): Response = {
  	log.debug("Responses built, error, data: %s" format info)
    val response = Response(Http11, InternalServerError)
    response.mediaType = "text/plain"
    if (gzip) response.setHeader(CONTENT_ENCODING, GZIP)
    response.content = content(info, gzip)
    response
  }

  def text(info: String, gzip: Boolean = false) = {
  	log.debug("Responses built, text, data: %s" format info)
    val response = Response(Http11, OK)
    response.mediaType = "text/plain"
    if (gzip) response.setHeader(CONTENT_ENCODING, GZIP)
    response.content = content(info, gzip)
    response
  }

  def html(info: String, gzip: Boolean = false) = {
  	log.debug("Responses built, text, data: %s" format info)
    val response = Response(Http11, OK)
    response.mediaType = "text/html"
    if (gzip) response.setHeader(CONTENT_ENCODING, GZIP)
    response.content = content(info, gzip)
    response
  }

  def content(data: String, gzip: Boolean = false) = {
  	log.debug("Responses built, content, data: %s" format data)
	if (gzip) {
      val bytes   = new ByteArrayOutputStream
      val gzipper = new GZIPOutputStream(bytes)
      gzipper write data.getBytes(UTF_8)
      gzipper.finish
      copiedBuffer(bytes.toByteArray)
    } else copiedBuffer(data, UTF_8)
  }
}
