package kr.kro.btr.config

import com.hazelcast.core.HazelcastInstance
import org.slf4j.LoggerFactory
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

/**
 * 애플리케이션 시작 시 캐시 초기화
 * 클래스 구조 변경으로 인한 직렬화 오류를 방지하기 위해 기존 캐시를 클리어합니다.
 */
@Component
class CacheWarmer(
    private val hazelcastInstance: HazelcastInstance
) {

    private val logger = LoggerFactory.getLogger(CacheWarmer::class.java)

    @EventListener(ApplicationReadyEvent::class)
    fun clearCachesOnStartup() {
        try {
            logger.info("Clearing all Hazelcast caches on application startup...")

            val cacheNames = listOf("user", "crew", "feed", "activity", "comment", "default")

            cacheNames.forEach { cacheName ->
                try {
                    val map = hazelcastInstance.getMap<Any, Any>(cacheName)
                    val size = map.size
                    map.clear()
                    logger.info("Cleared cache '$cacheName' (removed $size entries)")
                } catch (e: Exception) {
                    logger.warn("Failed to clear cache '$cacheName': ${e.message}")
                }
            }

            logger.info("All caches cleared successfully")
        } catch (e: Exception) {
            logger.error("Error clearing caches on startup", e)
        }
    }
}
