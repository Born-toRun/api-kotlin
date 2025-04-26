package kr.kro.btr.domain.port.model

import kr.kro.btr.support.TokenDetail


data class RemoveFeedCommand(
    val feedId: Long,
    val my: TokenDetail
)
