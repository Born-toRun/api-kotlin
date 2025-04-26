package kr.kro.btr.config

import com.hazelcast.spring.cache.HazelcastCacheManager
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@EnableCaching
@Configuration
class CachingConfig {

    @Bean
    fun cacheManagerCustomizer(): CacheManagerCustomizer<HazelcastCacheManager?> {
        return CacheManagerCustomizer { cacheManagerCustomizer: HazelcastCacheManager? -> }
    }
}