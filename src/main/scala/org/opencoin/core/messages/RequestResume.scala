package org.opencoin.core.messages

case class RequestResume(
    `type`: String = "request resume",
    message_reference: Int,
    transaction_reference: Int)