package kr.kro.btr.domain.port.model

import java.time.LocalDateTime

data class CommentDetail(
    val id: Long,
    val parentId: Long?,
    val feedId: Long?,
    val contents: String,
    val registeredAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val writer: Writer,
    val reCommentResults: List<CommentResult>
) {
    data class Writer(
        val userId: Long?,
        val userName: String?,
        val profileImageUri: String?,
        val crewName: String?,
        val isAdmin: Boolean,
        val isManager: Boolean
    )
}