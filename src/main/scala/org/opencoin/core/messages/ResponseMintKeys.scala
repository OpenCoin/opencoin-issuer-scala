package org.opencoin.core.messages

import org.opencoin.core.token.MintKeyCertificate

case class ResponseMintKeys(
    `type`: String = "response mint keys",
    message_reference: Int,
    status_code: Int,
    status_description: String,
    keys: List[MintKeyCertificate])
