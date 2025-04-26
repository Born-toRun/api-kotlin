package kr.kro.btr.domain.port.model

import kr.kro.btr.domain.constant.RecommendationType

data class CreateRecommendationCommand(
    val recommendationType: RecommendationType,
    val contentId: Long,
    val myUserId: Long
)