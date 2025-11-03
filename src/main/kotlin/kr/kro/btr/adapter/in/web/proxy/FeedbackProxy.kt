package kr.kro.btr.adapter.`in`.web.proxy

import kr.kro.btr.adapter.`in`.web.payload.CreateFeedbackRequest
import kr.kro.btr.base.extension.toCreateFeedbackCommand
import kr.kro.btr.domain.port.FeedbackPort
import org.springframework.stereotype.Component

@Component
class FeedbackProxy(
    private val feedbackPort: FeedbackPort
) {
    fun create(request: CreateFeedbackRequest, userId: Long) {
        val command = request.toCreateFeedbackCommand(userId)
        feedbackPort.create(command)
    }
}
