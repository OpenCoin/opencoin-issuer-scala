package org.opencoin.core.messages

case class RequestCddSerial(
    `type`: String = "request cdd serial",
    message_reference: Int)