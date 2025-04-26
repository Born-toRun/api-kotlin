package kr.kro.btr.domain.port.model

data class ModifyCommentCommand(
    val commentId: Long,
    val contents: String
)
