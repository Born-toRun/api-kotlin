package kr.kro.btr.domain.port.model

data class DetailCommentCommand(
    val commentId: Long,
    val myUserId: Long
)