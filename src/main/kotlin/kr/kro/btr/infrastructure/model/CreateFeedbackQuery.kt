package kr.kro.btr.infrastructure.model

import kr.kro.btr.domain.constant.FeedbackType

data class CreateFeedbackQuery(
    val userId: Long,
    val content: String,
    val feedbackType: FeedbackType
)
