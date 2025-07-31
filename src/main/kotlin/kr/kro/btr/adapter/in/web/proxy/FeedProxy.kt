package kr.kro.btr.adapter.`in`.web.proxy

import kr.kro.btr.adapter.`in`.web.payload.CreateFeedRequest
import kr.kro.btr.adapter.`in`.web.payload.ModifyFeedRequest
import kr.kro.btr.adapter.`in`.web.payload.SearchFeedRequest
import kr.kro.btr.base.extension.toCreateFeedCommand
import kr.kro.btr.base.extension.toModifyFeedCommand
import kr.kro.btr.base.extension.toRemoveFeedCommand
import kr.kro.btr.base.extension.toSearchAllFeedCommand
import kr.kro.btr.base.extension.toSearchFeedDetailCommand
import kr.kro.btr.domain.port.FeedPort
import kr.kro.btr.domain.port.model.result.FeedDetailResult
import kr.kro.btr.domain.port.model.result.FeedResult
import kr.kro.btr.support.TokenDetail
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component

@Component
@CacheConfig(cacheNames = ["feed"])
class FeedProxy(
    private val feedPort: FeedPort
) {
    @Cacheable(key = "'searchDetail: ' + #feedId + #my.id")
    fun searchDetail(my: TokenDetail, feedId: Long): FeedDetailResult {
        val command = my.toSearchFeedDetailCommand(feedId)
        return feedPort.searchDetail(command)
    }

    @Cacheable(
        key = "#request == null ? 'searchAll: ' + #my.id + #pageable.offset : " +
                "'searchAll: ' + #my.id + #request.hashCode() + #pageable.offset"
    )
    fun searchAll(
        request: SearchFeedRequest,
        my: TokenDetail,
        lastFeedId: Long,
        pageable: Pageable
    ): Page<FeedResult> {
        val command = request.toSearchAllFeedCommand(my, lastFeedId)
        return feedPort.searchAll(command, pageable)
    }

    // @DistributedLock(key = "'FeedView-'.concat(#id)", waitTime = 10L)
    fun increaseViewQty(feedId: Long) {
        feedPort.increaseViewQty(feedId)
    }

    @CacheEvict(allEntries = true)
    fun create(request: CreateFeedRequest, my: TokenDetail) {
        val command = request.toCreateFeedCommand(my)
        feedPort.create(command)
    }

    @CacheEvict(allEntries = true)
    fun remove(feedId: Long, my: TokenDetail) {
        val command = my.toRemoveFeedCommand(feedId)
        feedPort.remove(command)
    }

    @CacheEvict(allEntries = true)
    fun modify(request: ModifyFeedRequest, feedId: Long) {
        val command = request.toModifyFeedCommand(feedId)
        feedPort.modify(command)
    }
}
