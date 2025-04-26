package kr.kro.btr.adapter.`in`.web

import kr.kro.btr.adapter.`in`.web.payload.RecentSearchKeywordResponse
import kr.kro.btr.adapter.`in`.web.proxy.RecentSearchKeywordProxy
import kr.kro.btr.support.TokenDetail
import kr.kro.btr.support.annotation.AuthUser
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/recent-search-keywords")
class RecentSearchKeywordController(private val recentSearchKeywordProxy: RecentSearchKeywordProxy) {

    @PostMapping("/{keyword}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun addRecentSearchKeyword(@AuthUser my: TokenDetail, @PathVariable keyword: String) {
        if (my.isLogin()) {
            recentSearchKeywordProxy.add(my.id, keyword)
        }
    }

    @DeleteMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun removeAll(@AuthUser my: TokenDetail) {
        recentSearchKeywordProxy.removeAll(my.id)
    }

    @DeleteMapping("/{keyword}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun removeKeyword(@AuthUser my: TokenDetail, @PathVariable keyword: String) {
        recentSearchKeywordProxy.removeKeyword(my.id, keyword)
    }

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getRecentSearchKeyword(@AuthUser my: TokenDetail): ResponseEntity<RecentSearchKeywordResponse> {
        val response = if (!my.isLogin()) {
            val recentSearchKeywords = recentSearchKeywordProxy.search(my.id).toSet()
            RecentSearchKeywordResponse(recentSearchKeywords)
        } else {
            RecentSearchKeywordResponse(emptySet())
        }
        return ResponseEntity.ok(response)
    }
}


