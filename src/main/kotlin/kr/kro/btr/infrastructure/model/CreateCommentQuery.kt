package kr.kro.btr.infrastructure.model

data class CreateCommentQuery(
    val myUserId: Long,
    val feedId: Long?,
    val parentCommentId: Long?,
    val contents: String
)