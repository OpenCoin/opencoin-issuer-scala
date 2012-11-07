package org.opencoin.core.messages

case class ResponseMintKeys(
    `type`: String = "response mint keys",
    message_reference: Int,
    status_code: Int,
    status_description: String,
    keys: List[String])
