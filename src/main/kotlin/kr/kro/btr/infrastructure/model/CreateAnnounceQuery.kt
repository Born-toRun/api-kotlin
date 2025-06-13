package kr.kro.btr.infrastructure.model

import java.time.LocalDateTime

data class CreateAnnounceQuery(
    val title: String,
    val contents: String,
    val postedAt: LocalDateTime,
    val myUserId: Long
)
