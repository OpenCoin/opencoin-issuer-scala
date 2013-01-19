package org.opencoin.core.messages

import org.opencoin.core.token.Coin

case class RequestInvalidationREST(
    authorization: String,
    coins: List[Coin])