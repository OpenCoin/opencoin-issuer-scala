package org.opencoin.core.messages

abstract class Response(
    `type`: String,
    message_reference: Int,
    status_code: Int,
    status_description: String)