package org.opencoin.core.messages

import org.opencoin.core.token.Coin

case class SendCoins(
    `type`: String = "send coins",
    message_reference: Int,
    subject: String,
    coins: List[Coin])