package kr.kro.btr.infrastructure

import kr.kro.btr.adapter.out.persistence.FeedbackRepository
import kr.kro.btr.base.extension.toFeedbackEntity
import kr.kro.btr.infrastructure.model.CreateFeedbackQuery
import org.springframework.stereotype.Component

@Component
class FeedbackGateway(
    private val feedbackRepository: FeedbackRepository
) {
    fun create(query: CreateFeedbackQuery) {
        val feedbackEntity = query.toFeedbackEntity()
        feedbackRepository.save(feedbackEntity)
    }
}
