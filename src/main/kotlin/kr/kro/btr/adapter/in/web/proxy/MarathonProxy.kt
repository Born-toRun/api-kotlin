package kr.kro.btr.adapter.`in`.web.proxy

import kr.kro.btr.adapter.`in`.web.payload.SearchAllMarathonRequest
import kr.kro.btr.core.converter.MarathonConverter
import kr.kro.btr.domain.port.MarathonPort
import kr.kro.btr.domain.port.model.Marathon
import kr.kro.btr.domain.port.model.MarathonDetail
import kr.kro.btr.support.TokenDetail
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component

@Component
@CacheConfig(cacheNames = ["marathon"])
class MarathonProxy(
    private val marathonConverter: MarathonConverter,
    private val marathonPort: MarathonPort
) {

    @Cacheable(key = "'searchAll: ' + #request.hashCode() + #my.id")
    fun search(request: SearchAllMarathonRequest, my: TokenDetail): List<Marathon> {
        val command = marathonConverter.map(request, my.id)
        return marathonPort.search(command)
    }

    @Cacheable(key = "'search: ' + #marathonId + #my.id")
    fun detail(marathonId: Long, my: TokenDetail): MarathonDetail {
        val command = marathonConverter.mapToSearchMarathonDetailCommand(marathonId, my.id)
        return marathonPort.detail(command)
    }

    @CacheEvict(allEntries = true)
    fun bookmark(marathonId: Long, my: TokenDetail): Long {
        val command = marathonConverter.mapToBookmarkMarathonCommand(marathonId, my.id)
        return marathonPort.bookmark(command)
    }

    @CacheEvict(allEntries = true)
    fun cancelBookmark(marathonId: Long, my: TokenDetail): Long {
        val command = marathonConverter.mapToCancelBookmarkMarathonCommand(marathonId, my.id)
        return marathonPort.cancelBookmark(command)
    }
}
