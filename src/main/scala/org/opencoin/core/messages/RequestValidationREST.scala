package org.opencoin.core.messages

import org.opencoin.core.token.Blind

case class RequestValidationREST(
    reference: Int,
    authorization: String,
    blinds: List[Blind])