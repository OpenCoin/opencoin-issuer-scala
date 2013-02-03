package org.opencoin.core.messages

import org.opencoin.core.token.CDD

case class ResponseCdd(
    `type`: String = "response cdd",
    message_reference: Int,
    status_code: Int,
    status_description: String,
    cdd: CDD)