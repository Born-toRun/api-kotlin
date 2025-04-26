package kr.kro.btr.domain.port.model

import kr.kro.btr.domain.constant.FeedCategory
import kr.kro.btr.support.TokenDetail

data class SearchAllFeedCommand(
    val category: FeedCategory?,
    val searchKeyword: String?,
    val isMyCrew: Boolean,
    val my: TokenDetail,
    val lastFeedId: Long
)
