package kr.kro.btr.adapter.out.persistence

import kr.kro.btr.domain.constant.RecommendationType
import kr.kro.btr.domain.entity.RecommendationEntity
import org.springframework.data.jpa.repository.JpaRepository

interface RecommendationRepository : JpaRepository<RecommendationEntity, Long> {

    fun findByUserIdAndRecommendationTypeAndContentId(
        userId: Long,
        recommendationType: RecommendationType,
        contentId: Long
    ): RecommendationEntity?

    fun findAllByUserId(userId: Long): List<RecommendationEntity>

    fun findAllByRecommendationTypeAndContentIdIn(
        recommendationType: RecommendationType,
        contentIds: List<Long>
    ): List<RecommendationEntity>
}
