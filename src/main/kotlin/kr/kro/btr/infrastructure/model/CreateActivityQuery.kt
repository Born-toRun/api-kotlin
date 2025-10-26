package kr.kro.btr.infrastructure.model

import java.time.LocalDateTime

data class CreateActivityQuery(
    val imageIds: List<Long>?,
    val title: String,
    val contents: String,
    val startAt: LocalDateTime,
    val venue: String?,
    val venueUrl: String,
    val participantsLimit: Int,
    val participationFee: Int,
    val course: String?,
    val courseDetail: String?,
    val path: String?,
    val myUserId: Long
)
