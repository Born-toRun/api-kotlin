package kr.kro.btr.domain.port.model

data class SearchAllCommentCommand(
    val feedId: Long,
    val myUserId: Long
)