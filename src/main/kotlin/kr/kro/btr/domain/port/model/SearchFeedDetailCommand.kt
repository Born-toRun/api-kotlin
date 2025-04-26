package kr.kro.btr.domain.port.model

import kr.kro.btr.support.TokenDetail

data class SearchFeedDetailCommand(
    val my: TokenDetail,
    val feedId: Long
)