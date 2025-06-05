package kr.kro.btr.domain.port.model.result

import java.time.LocalDateTime

data class MarathonDetailResult(
    val id: Long,
    val title: String?,
    val owner: String?,
    val email: String?,
    val schedule: String?,
    val contact: String?,
    val course: String?,
    val location: String?,
    val venue: String?,
    val host: String?,
    val duration: String?,
    val homepage: String?,
    val venueDetail: String?,
    val remark: String?,
    val registeredAt: LocalDateTime,
    val isBookmarking: Boolean? = false
)
