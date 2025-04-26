package kr.kro.btr.adapter.`in`.web.payload

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class SearchActivityDetailResponse(
    val id: Long,
    val title: String,
    val contents: String,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val startDate: LocalDateTime,
    val venue: String?,
    val venueUrl: String,
    val participantsLimit: Int,
    val participantsQty: Int,
    val participationFee: Int,
    val course: String?,
    val courseDetail: String?,
    val path: String?,
    val host: Host,
    val isOpen: Boolean?,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val updatedAt: LocalDateTime,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val registeredAt: LocalDateTime
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
}
