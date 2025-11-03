package kr.kro.btr.domain.port.model

import kr.kro.btr.domain.constant.FeedbackType

data class CreateFeedbackCommand(
    val userId: Long,
    val content: String,
    val feedbackType: FeedbackType
)
