package kr.kro.btr.adapter.`in`.web.payload

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import kr.kro.btr.domain.constant.FeedbackType

data class CreateFeedbackRequest(
    @field:NotNull(message = "피드백 타입은 필수입니다.")
    val feedbackType: FeedbackType,

    @field:NotBlank(message = "내용은 필수입니다.")
    val content: String
)
