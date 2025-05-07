package kr.kro.btr.adapter.`in`.web.payload

import jakarta.validation.constraints.NotBlank

data class ModifyCommentRequest(
    @NotBlank
    val contents: String
)
