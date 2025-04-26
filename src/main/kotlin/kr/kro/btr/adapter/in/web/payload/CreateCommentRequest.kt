package kr.kro.btr.adapter.`in`.web.payload

import jakarta.validation.constraints.NotNull


data class CreateCommentRequest(
    val parentCommentId: Long?,
    @NotNull val contents: String
)
