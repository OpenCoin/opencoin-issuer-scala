package org.opencoin.core.messages

import org.opencoin.core.util.Base64

case class RequestMintKeys(
    `type`: String = "request mint keys",
    message_reference: Int,
    mint_key_ids: List[String], //for specific keys
    denominations: List[Int])