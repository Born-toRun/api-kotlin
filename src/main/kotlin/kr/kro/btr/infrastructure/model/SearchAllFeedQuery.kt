package kr.kro.btr.infrastructure.model

import kr.kro.btr.domain.constant.FeedCategory
import kr.kro.btr.support.TokenDetail

data class SearchAllFeedQuery(
    val category: FeedCategory?,
    val searchKeyword: String?,
    val isMyCrew: Boolean,
    val my: TokenDetail?,
    val lastFeedId: Long,
    val searchedUserIds: List<Long>?
)
