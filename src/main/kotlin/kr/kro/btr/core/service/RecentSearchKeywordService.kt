package kr.kro.btr.core.service

import kr.kro.btr.adapter.out.thirdparty.RedisClient
import kr.kro.btr.domain.port.RecentSearchKeywordPort
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RecentSearchKeywordService(
    private val redisClient: RedisClient
) : RecentSearchKeywordPort {

    @Async
    @Transactional
    override fun removeAll(userId: Long) {
        redisClient.removeAll("$RECENT_SEARCH_KEYWORD_KEY_PREFIX$userId")
    }

    @Async
    @Transactional
    override fun removeKeyword(userId: Long, searchKeyword: String) {
        redisClient.removeValue("$RECENT_SEARCH_KEYWORD_KEY_PREFIX$userId", searchKeyword)
    }

    @Async
    @Transactional
    override fun add(userId: Long, searchKeyword: String) {
        val key = "$RECENT_SEARCH_KEYWORD_KEY_PREFIX$userId"
        redisClient.add(key, searchKeyword)

        val recentSearchKeywords = redisClient.getList(key).toMutableList()

        val recentSearchKeywordMaxSize = 10
        if (recentSearchKeywords.size > recentSearchKeywordMaxSize) {
            recentSearchKeywords.removeAt(0)
        }
    }

    @Transactional(readOnly = true)
    override fun search(userId: Long): List<Any> {
        return redisClient.getList("$RECENT_SEARCH_KEYWORD_KEY_PREFIX$userId")
    }

    companion object {
        private const val RECENT_SEARCH_KEYWORD_KEY_PREFIX = "recentSearch:"
    }
}
