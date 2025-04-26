package kr.kro.btr.domain.port.model

import kr.kro.btr.domain.constant.RecommendationType

data class RemoveRecommendationCommand(
    val recommendationType: RecommendationType,
    val contentId: Long,
    val myUserId: Long
)
