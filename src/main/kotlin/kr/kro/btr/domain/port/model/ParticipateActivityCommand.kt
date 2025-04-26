package kr.kro.btr.domain.port.model

data class ParticipateActivityCommand(
    val activityId: Long,
    val myUserId: Long
)