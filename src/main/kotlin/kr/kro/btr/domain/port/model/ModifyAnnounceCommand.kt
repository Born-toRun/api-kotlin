package kr.kro.btr.domain.port.model

import java.time.LocalDateTime

data class ModifyAnnounceCommand(
    val id: Long,
    val title: String,
    val contents: String,
    val postedAt: LocalDateTime
)
