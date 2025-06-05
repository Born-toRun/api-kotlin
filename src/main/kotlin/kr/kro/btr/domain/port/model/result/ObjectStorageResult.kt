package kr.kro.btr.domain.port.model.result

import java.time.LocalDateTime

data class ObjectStorageResult(
    val id: Long,
    val userId: Long,
    val fileUri: String,
    val uploadAt: LocalDateTime
)
