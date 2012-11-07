package org.opencoin.core.messages

import org.opencoin.core.token.BlindSignature
import java.util.Date

case class ResponseValidation(
    `type`: String = "response validation",
    message_reference: Int,
    status_code: Int,
    status_description: String,
    retry_after: Date,
    blind_signatures: List[BlindSignature])