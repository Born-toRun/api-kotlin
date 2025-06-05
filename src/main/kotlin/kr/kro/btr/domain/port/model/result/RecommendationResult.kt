package kr.kro.btr.domain.port.model.result

import kr.kro.btr.domain.constant.RecommendationType
import java.time.LocalDateTime

data class RecommendationResult(
    val id: Long,
    val contentId: Long,
    val userId: Long,
    val recommendationType: RecommendationType,
    val registeredAt: LocalDateTime
)
