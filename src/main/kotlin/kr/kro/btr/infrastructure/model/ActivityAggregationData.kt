package kr.kro.btr.infrastructure.model

import kr.kro.btr.domain.entity.ActivityEntity
import java.time.LocalDateTime

data class ActivityAggregationData(
    val activityEntity: ActivityEntity,
    val participantsCount: Int,
    val hasUserParticipation: Boolean,
    val attendanceCode: Int? = null,
    val attendanceExpiresAt: LocalDateTime? = null
)
