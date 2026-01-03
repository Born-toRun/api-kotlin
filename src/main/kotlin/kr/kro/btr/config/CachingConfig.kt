package kr.kro.btr.config

import com.hazelcast.spring.cache.HazelcastCacheManager
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer
import org.springframework.cache.annotation.CachingConfigurer
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.interceptor.CacheErrorHandler
import org.springframework.cache.interceptor.SimpleCacheErrorHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@EnableCaching
@Configuration
class CachingConfig : CachingConfigurer {

    private val logger = LoggerFactory.getLogger(CachingConfig::class.java)

    @Bean
    fun cacheManagerCustomizer(): CacheManagerCustomizer<HazelcastCacheManager?> {
        return CacheManagerCustomizer { cacheManagerCustomizer: HazelcastCacheManager? -> }
    }

    /**
     * 직렬화/역직렬화 오류가 발생해도 애플리케이션이 계속 동작하도록 해야함
     */
    override fun errorHandler(): CacheErrorHandler {
        return object : SimpleCacheErrorHandler() {
            override fun handleCacheGetError(exception: RuntimeException, cache: org.springframework.cache.Cache, key: Any) {
                logger.error(
                    "Cache GET error - cache: ${cache.name}, key: $key, error: ${exception.message}",
                    exception
                )
            }

            override fun handleCachePutError(exception: RuntimeException, cache: org.springframework.cache.Cache, key: Any, value: Any?) {
                logger.error(
                    "Cache PUT error - cache: ${cache.name}, key: $key, value type: ${value?.javaClass?.name}, error: ${exception.message}",
                    exception
                )
            }

            override fun handleCacheEvictError(exception: RuntimeException, cache: org.springframework.cache.Cache, key: Any) {
                logger.error(
                    "Cache EVICT error - cache: ${cache.name}, key: $key, error: ${exception.message}",
                    exception
                )
            }

            override fun handleCacheClearError(exception: RuntimeException, cache: org.springframework.cache.Cache) {
                logger.error(
                    "Cache CLEAR error - cache: ${cache.name}, error: ${exception.message}",
                    exception
                )
            }
        }
    }
}
