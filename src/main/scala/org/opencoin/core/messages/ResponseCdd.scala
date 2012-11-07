package org.opencoin.core.messages

case class ResponseCdd(
    `type`: String = "response cdd",
    message_reference: Int,
    status_code: Int,
    status_description: String,
    cdd: String)