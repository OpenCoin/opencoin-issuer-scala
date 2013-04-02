package org.opencoin.core.messages

case class ResponseError(
    `type`: String,
    message_reference: Int,
    status_code: Int,
    status_description: String) extends Response