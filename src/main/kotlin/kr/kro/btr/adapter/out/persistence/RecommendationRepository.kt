package kr.kro.btr.adapter.out.persistence

import kr.kro.btr.domain.constant.RecommendationType
import kr.kro.btr.domain.entity.RecommendationEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface RecommendationRepository : JpaRepository<RecommendationEntity, Long> {

    fun findByUserIdAndRecommendationTypeAndContentId(
        userId: Long,
        recommendationType: RecommendationType,
        contentId: Long
    ): RecommendationEntity?

    fun countByContentIdAndRecommendationType(contentId: Long, recommendationType: RecommendationType): Int

    fun existsByContentIdAndUserIdAndRecommendationType(
        contentId: Long,
        userId: Long,
        recommendationType: RecommendationType
    ): Boolean

    @Query(
        """
        SELECT r.contentId as contentId, COUNT(r.id) as count
        FROM RecommendationEntity r
        WHERE r.contentId IN :contentIds
        AND r.recommendationType = :type
        GROUP BY r.contentId
        """
    )
    fun countGroupByContentId(contentIds: List<Long>, type: RecommendationType): List<RecommendationCount>

    @Query(
        """
        SELECT r.contentId
        FROM RecommendationEntity r
        WHERE r.userId = :userId
        AND r.contentId IN :contentIds
        AND r.recommendationType = :type
        """
    )
    fun findUserRecommendedContentIds(
        userId: Long,
        contentIds: List<Long>,
        type: RecommendationType
    ): List<Long>

    interface RecommendationCount {
        fun getContentId(): Long
        fun getCount(): Long
    }
}
