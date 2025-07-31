package kr.kro.btr.adapter.`in`.web

import kr.kro.btr.adapter.`in`.web.payload.BookmarkMarathonResponse
import kr.kro.btr.adapter.`in`.web.payload.DetailMarathonResponse
import kr.kro.btr.adapter.`in`.web.payload.SearchMarathonsRequest
import kr.kro.btr.adapter.`in`.web.payload.SearchMarathonsResponse
import kr.kro.btr.adapter.`in`.web.proxy.MarathonProxy
import kr.kro.btr.base.extension.toSearchAllMarathonResponse
import kr.kro.btr.base.extension.toSearchMarathonDetailResponse
import kr.kro.btr.domain.port.model.result.MarathonDetailResult
import kr.kro.btr.domain.port.model.result.MarathonResult
import kr.kro.btr.support.TokenDetail
import kr.kro.btr.support.annotation.AuthUser
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/marathons")
class MarathonController(
    private val marathonProxy: MarathonProxy
) {

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun searchAll(@AuthUser my: TokenDetail, @RequestParam(required = false) locations: List<String>?,
                  @RequestParam(required = false) courses: List<String>?): ResponseEntity<SearchMarathonsResponse> {
        val marathonResults: List<MarathonResult> = marathonProxy.search(SearchMarathonsRequest(locations, courses), my)
        val response = marathonResults.toSearchAllMarathonResponse()
        return ResponseEntity.ok(response)
    }

    @GetMapping("/{marathonId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun detail(@AuthUser my: TokenDetail, @PathVariable marathonId: Long): ResponseEntity<DetailMarathonResponse> {
        val marathonDetailResult: MarathonDetailResult = marathonProxy.detail(marathonId, my)
        val response: DetailMarathonResponse = marathonDetailResult.toSearchMarathonDetailResponse()
        return ResponseEntity.ok(response)
    }

    @PostMapping("/bookmark/{marathonId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun bookmark(@AuthUser my: TokenDetail, @PathVariable marathonId: Long): ResponseEntity<BookmarkMarathonResponse> {
        val bookmarkedMarathonId: Long = marathonProxy.bookmark(marathonId, my)
        val response = BookmarkMarathonResponse(bookmarkedMarathonId)
        return ResponseEntity.ok(response)
    }

    @DeleteMapping("/bookmark/{marathonId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun cancelBookmark(@AuthUser my: TokenDetail, @PathVariable marathonId: Long): ResponseEntity<BookmarkMarathonResponse> {
        val bookmarkCanceledMarathonId: Long = marathonProxy.cancelBookmark(marathonId, my)
        val response = BookmarkMarathonResponse(bookmarkCanceledMarathonId)
        return ResponseEntity.ok(response)
    }
}
