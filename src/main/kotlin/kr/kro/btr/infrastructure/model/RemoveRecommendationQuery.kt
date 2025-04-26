package kr.kro.btr.infrastructure.model

import kr.kro.btr.domain.constant.RecommendationType

data class RemoveRecommendationQuery(
    val recommendationType: RecommendationType,
    val contentId: Long,
    val myUserId: Long
)