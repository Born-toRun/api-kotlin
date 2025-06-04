package kr.kro.btr.adapter.`in`.web

import jakarta.validation.Valid
import kr.kro.btr.adapter.`in`.web.payload.CreateFeedRequest
import kr.kro.btr.adapter.`in`.web.payload.DetailFeedResponse
import kr.kro.btr.adapter.`in`.web.payload.ModifyFeedRequest
import kr.kro.btr.adapter.`in`.web.payload.SearchFeedRequest
import kr.kro.btr.adapter.`in`.web.payload.SearchFeedResponse
import kr.kro.btr.adapter.`in`.web.proxy.FeedProxy
import kr.kro.btr.base.extension.toDetailFeedResponse
import kr.kro.btr.base.extension.toSearchFeedResponse
import kr.kro.btr.domain.port.model.FeedCard
import kr.kro.btr.domain.port.model.FeedResult
import kr.kro.btr.support.TokenDetail
import kr.kro.btr.support.annotation.AuthUser
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/feeds")
class FeedController(
    private val feedProxy: FeedProxy
) {

    @GetMapping("{feedId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun detail(@AuthUser my: TokenDetail, @PathVariable feedId: Long): ResponseEntity<DetailFeedResponse> {
        val feedResult: FeedResult = feedProxy.searchDetail(my, feedId)
        feedProxy.increaseViewQty(feedId)
        val response = feedResult.toDetailFeedResponse()
        return ResponseEntity.ok(response)
    }

    @PostMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun create(@AuthUser my: TokenDetail, @RequestBody @Valid request: CreateFeedRequest): ResponseEntity<Void> {
        feedProxy.create(request, my)
        return ResponseEntity(CREATED)
    }

    @DeleteMapping("/{feedId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun remove(@AuthUser my: TokenDetail, @PathVariable feedId: Long): ResponseEntity<Void> {
        feedProxy.remove(feedId, my)
        return ResponseEntity.ok().build()
    }

    @PutMapping("/{feedId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun modify(@AuthUser my: TokenDetail, @PathVariable feedId: Long, @RequestBody @Valid request: ModifyFeedRequest): ResponseEntity<Void> {
        feedProxy.modify(request, feedId)
        return ResponseEntity.ok().build()
    }

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun searchAll(
        @AuthUser my: TokenDetail,
        @Valid @ModelAttribute request: SearchFeedRequest,
        @RequestParam(defaultValue = "0") lastFeedId: Long,
        @RequestParam(defaultValue = "10") size: Int
    ): ResponseEntity<Page<SearchFeedResponse>> {
        val feedPage: Page<FeedCard> = feedProxy.searchAll(request, my, lastFeedId, PageRequest.of(0, size))
        return ResponseEntity.ok(feedPage.map { it.toSearchFeedResponse() })
    }
}
