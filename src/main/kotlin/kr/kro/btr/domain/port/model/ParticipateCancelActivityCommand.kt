package kr.kro.btr.domain.port.model

data class ParticipateCancelActivityCommand(
    val participationId: Long,
    val myUserId: Long
)