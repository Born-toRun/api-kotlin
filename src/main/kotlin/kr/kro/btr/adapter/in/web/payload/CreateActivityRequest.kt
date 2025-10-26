package kr.kro.btr.adapter.`in`.web.payload

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

data class CreateActivityRequest(
    @Size(max = 5, message = "이미지는 최대 5개까지 업로드 가능합니다.")
    val imageIds: List<Long>?,
    @NotBlank
    val title: String,
    @NotBlank
    val contents: String,
    @JsonSerialize(using = LocalDateTimeSerializer::class)
    @NotNull
    val startAt: LocalDateTime,
    val venue: String?,
    @NotBlank
    val venueUrl: String,
    @NotNull
    val participantsLimit: Int,
    @NotNull
    val participationFee: Int,
    val course: String?,
    val courseDetail: String?,
    val path: String?
)
