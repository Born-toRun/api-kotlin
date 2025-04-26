package kr.kro.btr.adapter.`in`.web.payload

import jakarta.validation.constraints.NotNull
import kr.kro.btr.domain.constant.FeedAccessLevel
import kr.kro.btr.domain.constant.FeedCategory

data class CreateFeedRequest(
    val imageIds: List<Long>?,
    @NotNull val contents: String,
    @NotNull val category: FeedCategory,
    @NotNull val accessLevel: FeedAccessLevel
)
