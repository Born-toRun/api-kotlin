package kr.kro.btr.adapter.`in`.web

import jakarta.validation.Valid
import kr.kro.btr.adapter.`in`.web.payload.CreateFeedRequest
import kr.kro.btr.adapter.`in`.web.payload.DetailFeedResponse
import kr.kro.btr.adapter.`in`.web.payload.ModifyFeedRequest
import kr.kro.btr.adapter.`in`.web.payload.SearchFeedRequest
import kr.kro.btr.adapter.`in`.web.payload.SearchFeedResponse
import kr.kro.btr.adapter.`in`.web.proxy.FeedProxy
import kr.kro.btr.core.converter.FeedConverter
import kr.kro.btr.domain.port.model.FeedCard
import kr.kro.btr.domain.port.model.FeedResult
import kr.kro.btr.support.TokenDetail
import kr.kro.btr.support.annotation.AuthUser
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/feeds")
class FeedController(
    private val feedConverter: FeedConverter,
    private val feedProxy: FeedProxy
) {

    @GetMapping("{feedId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun detail(@AuthUser my: TokenDetail, @PathVariable feedId: Long): ResponseEntity<DetailFeedResponse> {
        val feedResult: FeedResult = feedProxy.searchDetail(my, feedId)
        feedProxy.increaseViewQty(feedId)
        return ResponseEntity.ok(feedConverter.map(feedResult))
    }

    @PostMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun create(@AuthUser my: TokenDetail, @RequestBody @Valid request: CreateFeedRequest) {
        feedProxy.create(request, my)
    }

    @DeleteMapping("/{feedId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun remove(@AuthUser my: TokenDetail, @PathVariable feedId: Long) {
        feedProxy.remove(feedId, my)
    }

    @PutMapping("/{feedId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun modify(@AuthUser my: TokenDetail, @PathVariable feedId: Long, @RequestBody @Valid request: ModifyFeedRequest) {
        feedProxy.modify(request, feedId)
    }

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun searchAll(
        @AuthUser my: TokenDetail,
        @Valid @ModelAttribute request: SearchFeedRequest,
        @RequestParam(defaultValue = "0") lastFeedId: Long,
        @RequestParam(defaultValue = "10") size: Int
    ): ResponseEntity<Page<SearchFeedResponse>> {
        val feedPage: Page<FeedCard> = feedProxy.searchAll(request, my, lastFeedId, PageRequest.of(0, size))
        return ResponseEntity.ok(feedPage.map { feedConverter.map(it) })
    }
}
