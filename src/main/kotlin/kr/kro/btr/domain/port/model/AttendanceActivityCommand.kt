package kr.kro.btr.domain.port.model

data class AttendanceActivityCommand(
    val activityId: Long,
    val accessCode: Int,
    val myUserId: Long
)