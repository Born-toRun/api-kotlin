package kr.kro.btr.domain.port.model

import java.time.LocalDateTime

data class ObjectStorage(
    val id: Long,
    val userId: Long,
    val fileUri: String,
    val uploadAt: LocalDateTime
)