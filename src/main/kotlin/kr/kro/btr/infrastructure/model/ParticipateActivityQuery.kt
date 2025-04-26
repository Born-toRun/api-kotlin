package kr.kro.btr.infrastructure.model

data class ParticipateActivityQuery(
    val activityId: Long,
    val myUserId: Long
)