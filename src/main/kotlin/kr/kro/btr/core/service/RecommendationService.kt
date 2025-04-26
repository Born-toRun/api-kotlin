package kr.kro.btr.core.service

import kr.kro.btr.core.converter.RecommendationConverter
import kr.kro.btr.domain.port.RecommendationPort
import kr.kro.btr.domain.port.model.CreateRecommendationCommand
import kr.kro.btr.domain.port.model.RemoveRecommendationCommand
import kr.kro.btr.infrastructure.RecommendationGateway
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RecommendationService(
    private val recommendationConverter: RecommendationConverter,
    private val recommendationGateway: RecommendationGateway
) : RecommendationPort {

    @Transactional
    override fun create(command: CreateRecommendationCommand) {
        val query = recommendationConverter.map(command)
        recommendationGateway.create(query)
    }

    @Transactional
    override fun remove(command: RemoveRecommendationCommand) {
        val query = recommendationConverter.map(command)
        recommendationGateway.remove(query)
    }
}
