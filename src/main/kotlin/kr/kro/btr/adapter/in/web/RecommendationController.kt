package kr.kro.btr.adapter.`in`.web

import kr.kro.btr.adapter.`in`.web.proxy.RecommendationProxy
import kr.kro.btr.domain.constant.RecommendationType
import kr.kro.btr.support.TokenDetail
import kr.kro.btr.support.annotation.AuthUser
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/recommendations")
class RecommendationController(private val recommendationProxy: RecommendationProxy) {

    @PostMapping("/{recommendationType}/{contentId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun recommend(
        @AuthUser my: TokenDetail,
        @PathVariable recommendationType: RecommendationType,
        @PathVariable contentId: Long
    ): ResponseEntity<Void> {
        recommendationProxy.create(my, recommendationType, contentId)
        return ResponseEntity(CREATED)
    }

    @DeleteMapping("/{recommendationType}/{contentId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun remove(
        @AuthUser my: TokenDetail,
        @PathVariable recommendationType: RecommendationType,
        @PathVariable contentId: Long
    ): ResponseEntity<Void> {
        recommendationProxy.remove(my, recommendationType, contentId)
        return ResponseEntity.ok().build()
    }
}
