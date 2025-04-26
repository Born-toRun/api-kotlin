package kr.kro.btr.domain.port.model

import kr.kro.btr.domain.constant.FeedAccessLevel
import kr.kro.btr.domain.constant.FeedCategory

data class ModifyFeedCommand(
    val imageIds: List<Long>?,
    val contents: String,
    val category: FeedCategory,
    val accessLevel: FeedAccessLevel,
    val feedId: Long
)
