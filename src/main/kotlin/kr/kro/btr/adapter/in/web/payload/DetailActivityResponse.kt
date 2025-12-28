package kr.kro.btr.adapter.`in`.web.payload

import com.fasterxml.jackson.annotation.JsonFormat
import kr.kro.btr.domain.constant.ActivityRecruitmentType
import java.time.LocalDateTime

data class DetailActivityResponse(
    val id: Long,
    val title: String,
    val contents: String,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val startAt: LocalDateTime,
    val venue: String?,
    val venueUrl: String,
    val participantsLimit: Int?,
    val participantsQty: Int,
    val participationFee: Int,
    val course: String?,
    val courseDetail: String?,
    val path: String?,
    val host: Host,
    val attendanceCode: Int?,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val attendanceExpiresAt: LocalDateTime?,
    val isOpen: Boolean,
    val isMyActivity: Boolean,
    val isParticipating: Boolean,
    val recruitmentType: ActivityRecruitmentType?,
    val imageUrls: List<String>,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val updatedAt: LocalDateTime,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val registeredAt: LocalDateTime
) {
    val entryFee: Int get() = participationFee
    val routeImageUrl: String? get() = path

    data class Host(
        val userId: Long,
        val crewId: Long,
        val profileImageUri: String?,
        val userName: String,
        val crewName: String,
        val isManager: Boolean,
        val isAdmin: Boolean
    )
}
