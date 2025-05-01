package kr.kro.btr.adapter.`in`.web.payload

import com.fasterxml.jackson.annotation.JsonFormat
import kr.kro.btr.domain.constant.ActivityRecruitmentType
import java.time.LocalDateTime

data class SearchActivityResponse(
    val activities: List<Activity>
) {

    data class Host(
        val userId: Long?,
        val crewId: Long?,
        val userProfileUri: String?,
        val userName: String?,
        val crewName: String?,
        val isManager: Boolean?,
        val isAdmin: Boolean?
    )

    data class Activity(
        val id: Long,
        val title: String,
        val host: Host,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        val startAt: LocalDateTime,
        val course: String?,
        val participantsLimit: Int?,
        val participantsQty: Int,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        val updatedAt: LocalDateTime,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        val registeredAt: LocalDateTime,
        val isOpen: Boolean?,
        val recruitmentType: ActivityRecruitmentType?
    )
}
