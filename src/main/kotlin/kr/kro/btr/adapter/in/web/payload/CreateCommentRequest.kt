package kr.kro.btr.adapter.`in`.web.payload

import jakarta.validation.constraints.NotBlank

data class CreateCommentRequest(
    val parentCommentId: Long?,
    @NotBlank val contents: String
)
