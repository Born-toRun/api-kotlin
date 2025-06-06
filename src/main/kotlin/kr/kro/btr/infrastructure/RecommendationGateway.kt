package kr.kro.btr.infrastructure

import kr.kro.btr.adapter.out.persistence.RecommendationRepository
import kr.kro.btr.base.extension.toRecommendationEntity
import kr.kro.btr.domain.entity.RecommendationEntity
import kr.kro.btr.infrastructure.model.CreateRecommendationQuery
import kr.kro.btr.infrastructure.model.RemoveRecommendationQuery
import kr.kro.btr.infrastructure.model.SearchAllRecommendationQuery
import kr.kro.btr.support.exception.NotFoundException
import org.springframework.stereotype.Component

@Component
class RecommendationGateway(
    private val recommendationRepository: RecommendationRepository,
) {

    fun searchAll(query: SearchAllRecommendationQuery): List<RecommendationEntity> {
        return recommendationRepository.findAllByRecommendationTypeAndContentIdIn(
            query.recommendationType,
            query.contentIds
        )
    }

    fun create(query: CreateRecommendationQuery) {
        val recommendationEntity = recommendationRepository
            .findByUserIdAndRecommendationTypeAndContentId(
                query.myUserId,
                query.recommendationType,
                query.contentId
            ) ?: query.toRecommendationEntity()

        recommendationRepository.save(recommendationEntity)
    }

    fun remove(query: RemoveRecommendationQuery) {
        val recommendationEntity = recommendationRepository
            .findByUserIdAndRecommendationTypeAndContentId(
                query.myUserId,
                query.recommendationType,
                query.contentId
            ) ?: throw NotFoundException("좋아요를 하지 않았습니다.")
        recommendationRepository.deleteById(recommendationEntity.id)
    }

    fun removeAll(userId: Long) {
        val ids = recommendationRepository.findAllByUserId(userId)
            .map { it.id }
        recommendationRepository.deleteAllById(ids)
    }
}
