package kr.kro.btr.adapter.`in`.web.payload

import kr.kro.btr.domain.constant.FeedAccessLevel
import kr.kro.btr.domain.constant.FeedCategory

data class ModifyFeedRequest(
    val imageIds: List<Long>?,
    val contents: String,
    val category: FeedCategory,
    val accessLevel: FeedAccessLevel
)
