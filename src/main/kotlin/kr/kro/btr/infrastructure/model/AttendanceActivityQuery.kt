package kr.kro.btr.infrastructure.model

data class AttendanceActivityQuery(
    val activityId: Long,
    val accessCode: Int,
    val myUserId: Long
)