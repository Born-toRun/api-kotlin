package kr.kro.btr.adapter.`in`.web.payload

import kr.kro.btr.domain.constant.FeedCategory

data class SearchFeedRequest(
    val category: FeedCategory?,
    val searchKeyword: String?,
    val isMyCrew: Boolean = false
)
