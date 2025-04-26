package kr.kro.btr.infrastructure.model

import kr.kro.btr.domain.constant.FeedAccessLevel
import kr.kro.btr.domain.constant.FeedCategory

data class CreateFeedQuery(
    val contents: String,
    val imageIds: List<Long>,
    val category: FeedCategory,
    val accessLevel: FeedAccessLevel,
    val userId: Long
)
