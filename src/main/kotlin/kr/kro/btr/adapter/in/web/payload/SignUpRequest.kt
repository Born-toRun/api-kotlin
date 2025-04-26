package kr.kro.btr.adapter.`in`.web.payload

import jakarta.validation.constraints.NotNull

data class SignUpRequest(
    @NotNull
    val userName: String,
    @NotNull
    val crewId: Long,
    val instagramId: String?
)
