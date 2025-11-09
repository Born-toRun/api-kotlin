package kr.kro.btr.infrastructure.model

import kr.kro.btr.domain.entity.FeedEntity

data class FeedAggregationData(
    val feedEntity: FeedEntity,
    val recommendationCount: Int,
    val hasUserRecommendation: Boolean,
    val commentCount: Int,
    val hasUserComment: Boolean
)
