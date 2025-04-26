package kr.kro.btr.adapter.`in`.web.payload

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime

data class ModifyActivityRequest(
        @NotNull
        val title: String,
        @NotNull
        val contents: String,
        @JsonSerialize(using = LocalDateTimeSerializer::class)
        @NotNull
        val startAt: LocalDateTime,
        val venue: String? = null,
        @NotNull
        val venueUrl: String,
        @NotNull
        val participantsLimit: Int,
        @NotNull
        val participationFee: Int = 0,
        val course: String? = null,
        val courseDetail: String? = null,
        val path: String? = null
)
