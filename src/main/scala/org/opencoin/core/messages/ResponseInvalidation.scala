package org.opencoin.core.messages

case class ResponseInvalidation(
    `type`: String = "response invalidation",
    message_reference: Int,
    status_code: Int,
    status_description: String) extends Response