package kr.kro.btr.core.service

import kr.kro.btr.base.extension.toCreateFeedbackQuery
import kr.kro.btr.domain.port.FeedbackPort
import kr.kro.btr.domain.port.model.CreateFeedbackCommand
import kr.kro.btr.infrastructure.FeedbackGateway
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class FeedbackService(
    private val feedbackGateway: FeedbackGateway
) : FeedbackPort {

    @Transactional
    override fun create(command: CreateFeedbackCommand) {
        val query = command.toCreateFeedbackQuery()
        feedbackGateway.create(query)
    }
}
