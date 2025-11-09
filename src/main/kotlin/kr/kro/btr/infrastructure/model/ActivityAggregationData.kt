package kr.kro.btr.infrastructure.model

import kr.kro.btr.domain.entity.ActivityEntity

data class ActivityAggregationData(
    val activityEntity: ActivityEntity,
    val participantsCount: Int,
    val hasUserParticipation: Boolean
)
