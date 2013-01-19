package org.opencoin.core.messages

import org.opencoin.core.token.{Blind,Coin}

case class RequestRenewalREST(
    reference: Int,
    coins: List[Coin],
    blinds: List[Blind])