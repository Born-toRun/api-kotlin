package kr.kro.btr.adapter.`in`.web.payload

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class OpenActivityResponse(
    val activityId: Long,
    val attendanceCode: Int,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val expiresAt: LocalDateTime?
)
