package org.opencoin.core.messages

import org.opencoin.core.token.BlindSignature

case class ResponseMinting(
    `type`: String = "response minting",
    message_reference: Int,
    status_code: Int,
    status_description: String,
    retry_after: java.util.Date,
    blind_signatures: List[BlindSignature]) extends Response