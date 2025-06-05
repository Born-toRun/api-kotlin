package kr.kro.btr.core.service

import kr.kro.btr.base.extension.toCreateRecommendationQuery
import kr.kro.btr.base.extension.toRemoveRecommendationQuery
import kr.kro.btr.domain.port.RecommendationPort
import kr.kro.btr.domain.port.model.CreateRecommendationCommand
import kr.kro.btr.domain.port.model.RemoveRecommendationCommand
import kr.kro.btr.infrastructure.RecommendationGateway
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RecommendationService(
    private val recommendationGateway: RecommendationGateway
) : RecommendationPort {

    @Transactional
    override fun create(command: CreateRecommendationCommand) {
        val query = command.toCreateRecommendationQuery()
        recommendationGateway.create(query)
    }

    @Transactional
    override fun remove(command: RemoveRecommendationCommand) {
        val query = command.toRemoveRecommendationQuery()
        recommendationGateway.remove(query)
    }
}
