package kr.kro.btr.domain.model

import java.time.LocalDateTime

data class ModifyAnnounceQuery(
    val id: Long,
    val title: String,
    val contents: String,
    val postedAt: LocalDateTime
)
