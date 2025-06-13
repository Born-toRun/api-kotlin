package kr.kro.btr.domain.port.model

import java.time.LocalDateTime

data class CreateAnnounceCommand(
    val title: String,
    val contents: String,
    val postedAt: LocalDateTime,
    val userId: Long
)
