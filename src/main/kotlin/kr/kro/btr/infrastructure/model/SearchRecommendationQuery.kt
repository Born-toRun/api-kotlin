package kr.kro.btr.infrastructure.model

import kr.kro.btr.domain.constant.RecommendationType

data class SearchRecommendationQuery(
    val recommendationType: RecommendationType?,
    val contentId: Long
)