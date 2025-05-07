package kr.kro.btr.adapter.`in`.web.payload

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime

data class ModifyActivityRequest(
    @NotBlank
    val title: String,
    @NotBlank
    val contents: String,
    @JsonSerialize(using = LocalDateTimeSerializer::class)
    @NotNull
    val startAt: LocalDateTime,
    val venue: String? = null,
    @NotBlank
    val venueUrl: String,
    @NotNull
    val participantsLimit: Int,
    @NotNull
    val participationFee: Int = 0,
    val course: String? = null,
    val courseDetail: String? = null,
    val path: String? = null
)
