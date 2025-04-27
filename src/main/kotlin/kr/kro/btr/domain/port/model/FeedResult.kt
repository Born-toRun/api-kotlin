package kr.kro.btr.domain.port.model

import kr.kro.btr.domain.constant.FeedAccessLevel
import kr.kro.btr.domain.constant.FeedCategory
import java.time.LocalDateTime

data class FeedResult(
    val id: Long,
    val contents: String,
    val images: List<Image>,
    val category: FeedCategory,
    val accessLevel: FeedAccessLevel,
    val viewQty: Int,
    val registeredAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val writer: Writer,
    val recommendationQty: Int,
    val hasMyRecommendation: Boolean,
    val commentQty: Int,
    val hasMyComment: Boolean
) {
    data class Image(
        val id: Long?,
        val imageUri: String?
    )

    data class Writer(
        val userId: Long?,
        val userName: String?,
        val crewName: String?,
        val profileImageUri: String?,
        val isAdmin: Boolean?,
        val isManager: Boolean?
    )
}
