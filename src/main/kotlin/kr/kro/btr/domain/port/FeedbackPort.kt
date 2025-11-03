package kr.kro.btr.domain.port

import kr.kro.btr.domain.port.model.CreateFeedbackCommand

interface FeedbackPort {
    fun create(command: CreateFeedbackCommand)
}
