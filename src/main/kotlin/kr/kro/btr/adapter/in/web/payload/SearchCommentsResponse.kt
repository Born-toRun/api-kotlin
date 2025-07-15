package kr.kro.btr.adapter.`in`.web.payload

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class SearchCommentsResponse(
    val details: List<Comment>
) {
    data class Comment(
        val id: Long,
        val parentId: Long?,
        val reCommentQty: Int,
        val writer: Writer,
        val contents: String,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        val registeredAt: LocalDateTime,
        val isMyComment: Boolean = false
    ) {
        fun isReComment(): Boolean = parentId != null
    }

    data class Writer(
        val userId: Long,
        val userName: String,
        val profileImageUri: String?,
        val crewName: String,
        val isAdmin: Boolean,
        val isManager: Boolean
    )
}
