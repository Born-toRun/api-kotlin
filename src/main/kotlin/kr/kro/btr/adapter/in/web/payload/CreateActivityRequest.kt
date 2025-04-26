package kr.kro.btr.adapter.`in`.web.payload

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime

data class CreateActivityRequest(
    @NotNull
    val title: String,
    @NotNull
    val contents: String,
    @JsonSerialize(using = LocalDateTimeSerializer::class)
    @NotNull
    val startAt: LocalDateTime,
    val venue: String?,
    @NotNull
    val venueUrl: String,
    @NotNull
    val participantsLimit: Int,
    @NotNull
    val participationFee: Int,
    val course: String?,
    val courseDetail: String?,
    val path: String?
)