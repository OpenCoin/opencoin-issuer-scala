package org.opencoin.core.messages

import org.opencoin.core.token.BlindSignature
import java.util.Date

case class ResponseRenewal(
    `type`: String = "response renewal",
    message_reference: Int,
    status_code: Int,
    status_description: String,
    retry_after: Date,
    blind_signatures: List[BlindSignature])