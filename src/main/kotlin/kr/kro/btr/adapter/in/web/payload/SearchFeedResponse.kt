package kr.kro.btr.adapter.`in`.web.payload

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class SearchFeedResponse(
    val id: Long,
    val imageUris: List<String>? = null,
    val contents: String,
    val viewQty: Int,
    val recommendationQty: Int,
    val commentQty: Int,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val registeredAt: LocalDateTime,
    val writer: Writer,
    val viewer: Viewer
) {
    data class Writer(
        val userName: String,
        val crewName: String,
        val profileImageUri: String?,
        val isAdmin: Boolean,
        val isManager: Boolean
    )

    data class Viewer(
        val hasMyRecommendation: Boolean,
        val hasMyComment: Boolean
    )
}
