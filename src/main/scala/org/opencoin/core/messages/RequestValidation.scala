package org.opencoin.core.messages

import org.opencoin.core.token.Blind

case class RequestValidation(
    `type`: String = "request validation",
    message_reference: Int,
    transaction_reference: Int,
    authorization_info: String,
    blinds: List[Blind])