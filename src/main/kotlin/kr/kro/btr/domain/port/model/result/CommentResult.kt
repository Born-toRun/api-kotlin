package kr.kro.btr.domain.port.model.result

import java.time.LocalDateTime

data class CommentResult(
    val id: Long,
    val parentId: Long? = null,
    val reCommentQty: Int = 0,
    val feedId: Long?,
    val contents: String,
    val registeredAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val writer: Writer,
    val isMyComment: Boolean = false
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
