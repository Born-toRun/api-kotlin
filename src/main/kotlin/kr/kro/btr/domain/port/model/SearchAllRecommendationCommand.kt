package kr.kro.btr.domain.port.model

import kr.kro.btr.domain.constant.RecommendationType

data class SearchAllRecommendationCommand(
    val recommendationType: RecommendationType,
    val contentIds: List<Long>
)

