package kr.kro.btr.adapter.`in`.web.proxy

import kr.kro.btr.domain.port.RecentSearchKeywordPort
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component

@Component
@CacheConfig(cacheNames = ["recentSearchKeyword"])
class RecentSearchKeywordProxy(
    private val recentSearchKeywordPort: RecentSearchKeywordPort
) {

    @CacheEvict(allEntries = true)
    fun removeAll(userId: Long) {
        recentSearchKeywordPort.removeAll(userId)
    }

    @CacheEvict(allEntries = true)
    fun removeKeyword(userId: Long, searchKeyword: String) {
        recentSearchKeywordPort.removeKeyword(userId, searchKeyword)
    }

    @CacheEvict(allEntries = true)
    fun add(userId: Long, searchKeyword: String) {
        recentSearchKeywordPort.add(userId, searchKeyword)
    }

    @Cacheable(key = "'search: ' + #userId")
    fun search(userId: Long): List<Any> {
        return recentSearchKeywordPort.search(userId)
    }
}
