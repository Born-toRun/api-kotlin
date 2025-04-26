package kr.kro.btr.adapter.`in`.web.payload

import jakarta.validation.constraints.NotNull

data class ModifyCommentRequest(
    @NotNull
    val contents: String
)
