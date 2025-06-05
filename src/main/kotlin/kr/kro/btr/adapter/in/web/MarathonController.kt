package kr.kro.btr.adapter.`in`.web

import kr.kro.btr.adapter.`in`.web.payload.BookmarkMarathonResponse
import kr.kro.btr.adapter.`in`.web.payload.SearchAllMarathonRequest
import kr.kro.btr.adapter.`in`.web.payload.SearchAllMarathonResponse
import kr.kro.btr.adapter.`in`.web.payload.SearchMarathonDetailResponse
import kr.kro.btr.adapter.`in`.web.proxy.MarathonProxy
import kr.kro.btr.base.extension.toSearchAllMarathonResponse
import kr.kro.btr.base.extension.toSearchMarathonDetailResponse
import kr.kro.btr.domain.port.model.Marathon
import kr.kro.btr.domain.port.model.MarathonDetail
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
                  @RequestParam(required = false) courses: List<String>?): ResponseEntity<SearchAllMarathonResponse> {
        val marathons: List<Marathon> = marathonProxy.search(SearchAllMarathonRequest(locations, courses), my)
        val response = marathons.toSearchAllMarathonResponse()
        return ResponseEntity.ok(response)
    }

    @GetMapping("/{marathonId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun search(@AuthUser my: TokenDetail, @PathVariable marathonId: Long): ResponseEntity<SearchMarathonDetailResponse> {
        val marathonDetail: MarathonDetail = marathonProxy.detail(marathonId, my)
        val response: SearchMarathonDetailResponse = marathonDetail.toSearchMarathonDetailResponse()
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
