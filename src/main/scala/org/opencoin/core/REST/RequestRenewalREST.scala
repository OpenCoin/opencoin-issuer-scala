package org.opencoin.core.REST

import org.opencoin.core.token.{Blind,Coin}

case class RequestRenewalREST(
    reference: Int,
    coins: List[Coin],
    blinds: List[Blind])