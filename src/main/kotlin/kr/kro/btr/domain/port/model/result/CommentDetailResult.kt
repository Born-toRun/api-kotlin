package kr.kro.btr.domain.port.model.result

import java.time.LocalDateTime

data class CommentDetailResult(
    val id: Long,
    val parentId: Long? = null,
    val feedId: Long?,
    val contents: String,
    val registeredAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val writer: Writer,
    val reCommentResults: List<CommentResult>
) {
    data class Writer(
        val userId: Long,
        val userName: String,
        val profileImageUri: String?,
        val crewName: String,
        val isAdmin: Boolean,
        val isManager: Boolean
    )
}
