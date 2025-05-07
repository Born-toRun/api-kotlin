package kr.kro.btr.adapter.`in`.web.payload

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class CreateYellowCardRequest(
    @NotNull val targetUserId: Long,
    val reason: String?,
    @NotBlank val basis: String
)
