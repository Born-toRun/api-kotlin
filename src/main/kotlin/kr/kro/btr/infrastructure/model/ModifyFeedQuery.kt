package kr.kro.btr.infrastructure.model

import kr.kro.btr.domain.constant.FeedAccessLevel
import kr.kro.btr.domain.constant.FeedCategory

data class ModifyFeedQuery(
    val feedId: Long,
    val imageIds: List<Long>,
    val contents: String,
    val category: FeedCategory,
    val accessLevel: FeedAccessLevel
)