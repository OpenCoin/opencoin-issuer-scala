package org.opencoin.core.REST

import org.opencoin.core.token.Blind

case class RequestValidationREST(
    reference: Int,
    authorization: String,
    blinds: List[Blind])