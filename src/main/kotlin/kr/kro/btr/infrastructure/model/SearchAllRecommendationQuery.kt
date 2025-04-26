package kr.kro.btr.infrastructure.model

import kr.kro.btr.domain.constant.RecommendationType

data class SearchAllRecommendationQuery(
    val recommendationType: RecommendationType,
    val contentIds: MutableList<Long>
)