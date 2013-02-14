package org.opencoin.core.REST

import org.opencoin.core.token.Coin

case class RequestInvalidationREST(
    authorization: String,
    coins: List[Coin])