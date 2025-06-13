package kr.kro.btr.adapter.`in`.web.payload

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

data class ModifyAnnounceRequest(
    @NotBlank
    @Size(max = 64)
    val title: String,

    @NotBlank
    @Size(max = 1000)
    val contents: String,

    @JsonSerialize(using = LocalDateTimeSerializer::class)
    @NotNull
    val postedAt: LocalDateTime
)
