package kr.kro.btr.core.converter

import kr.kro.btr.adapter.`in`.web.payload.CreateYellowCardRequest
import kr.kro.btr.domain.constant.RecommendationType
import kr.kro.btr.domain.port.model.CreateRecommendationCommand
import kr.kro.btr.domain.port.model.CreateYellowCardCommand
import kr.kro.btr.domain.port.model.RemoveRecommendationCommand
import kr.kro.btr.infrastructure.model.CreateRecommendationQuery
import kr.kro.btr.infrastructure.model.RemoveRecommendationQuery
import org.springframework.stereotype.Component

@Component
class RecommendationConverter {

    fun map(source: CreateYellowCardRequest, userId: Long): CreateYellowCardCommand {
        return CreateYellowCardCommand(
            targetUserId = source.targetUserId,
            sourceUserId = userId,
            reason = source.reason,
            basis = source.basis
        )
    }

    fun mapToCreateRecommendationCommand(userId: Long, recommendationType: RecommendationType, contentId: Long): CreateRecommendationCommand {
        return CreateRecommendationCommand(
            recommendationType = recommendationType,
            contentId = contentId,
            myUserId = userId
        )
    }

    fun mapToRemoveRecommendationCommand(userId: Long, recommendationType: RecommendationType, contentId: Long): RemoveRecommendationCommand {
        return RemoveRecommendationCommand(
            recommendationType = recommendationType,
            contentId = contentId,
            myUserId = userId
        )
    }

    fun map(source: CreateRecommendationCommand): CreateRecommendationQuery {
        return CreateRecommendationQuery(
            recommendationType = source.recommendationType,
            contentId = source.contentId,
            myUserId = source.myUserId
        )
    }

    fun map(source: RemoveRecommendationCommand): RemoveRecommendationQuery {
        return RemoveRecommendationQuery(
            recommendationType = source.recommendationType,
            contentId = source.contentId,
            myUserId = source.myUserId
        )
    }
}