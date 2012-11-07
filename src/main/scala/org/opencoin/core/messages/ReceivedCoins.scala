package org.opencoin.core.messages

case class ReceivedCoins(
    `type`: String = "received coins",
    message_reference: Int,
    status_code: Int,
    status_description: String)