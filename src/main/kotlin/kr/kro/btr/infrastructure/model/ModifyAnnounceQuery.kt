package kr.kro.btr.infrastructure.model

import java.time.LocalDateTime

data class ModifyAnnounceQuery(
    val id: Long,
    val title: String,
    val contents: String,
    val postedAt: LocalDateTime
)
