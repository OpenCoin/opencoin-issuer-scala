package org.opencoin.core.messages

case class RequestCdd(
    `type`: String = "request cdd",
    message_reference: Int,
    cdd_serial: Int)