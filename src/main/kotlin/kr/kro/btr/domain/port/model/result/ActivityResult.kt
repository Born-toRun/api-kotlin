package kr.kro.btr.domain.port.model.result

import kr.kro.btr.domain.constant.ActivityRecruitmentType
import java.time.LocalDateTime

data class ActivityResult(
    val id: Long,
    val title: String,
    val contents: String,
    val startAt: LocalDateTime,
    val venue: String?,
    val venueUrl: String,
    val participantsLimit: Int,
    val participantsQty: Int,
    val participationFee: Int,
    val course: String?,
    val courseDetail: String?,
    val path: String?,
    val attendanceCode: Int = 0,
    val isOpen: Boolean,
    val updatedAt: LocalDateTime,
    val registeredAt: LocalDateTime,
    val recruitmentType: ActivityRecruitmentType? = null,
    val imageUrls: List<String> = emptyList(),
    val host: Host
) {
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
