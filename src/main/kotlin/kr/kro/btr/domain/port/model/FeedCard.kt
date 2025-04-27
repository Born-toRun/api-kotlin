package kr.kro.btr.domain.port.model

import java.time.LocalDateTime

data class FeedCard(
    val id: Long,
    val imageUris: List<String>,
    val contents: String,
    val viewQty: Int,
    val recommendationQty: Int,
    val commentQty: Int,
    val registeredAt: LocalDateTime,
    val writer: Writer,
    val hasRecommendation: Boolean,
    val hasComment: Boolean
) {
    data class Writer(
        val userName: String?,
        val crewName: String?,
        val profileImageUri: String?,
        val isAdmin: Boolean?,
        val isManager: Boolean?
    )
}
