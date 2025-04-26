package kr.kro.btr.domain.port.model

data class CreateCommentCommand(
    val myUserId: Long,
    val feedId: Long?,
    val parentCommentId: Long?,
    val contents: String
)