package kr.kro.btr.adapter.`in`.web

import jakarta.validation.Valid
import kr.kro.btr.adapter.`in`.web.payload.CreateFeedbackRequest
import kr.kro.btr.adapter.`in`.web.proxy.FeedbackProxy
import kr.kro.btr.support.TokenDetail
import kr.kro.btr.support.annotation.AuthUser
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/feedbacks")
class FeedbackController(
    private val feedbackProxy: FeedbackProxy
) {

    @PostMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun create(
        @AuthUser my: TokenDetail,
        @Valid @RequestBody request: CreateFeedbackRequest
    ): ResponseEntity<Void> {
        feedbackProxy.create(request, my.id)
        return ResponseEntity(HttpStatus.CREATED)
    }
}
