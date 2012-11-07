package org.opencoin.core.messages

import org.opencoin.core.token.Coin

case class RequestInvalidation(
    `type`: String = "request invalidation",
    message_reference: Int,
    authorization_info: String,
    coins: List[Coin])