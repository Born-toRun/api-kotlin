package kr.kro.btr.config

import kr.kro.btr.config.properties.RedisProperties
import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.config.Config
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration


@Configuration
@EnableConfigurationProperties(RedisProperties::class)
internal class RedissonConfig {

    private val redisProperties: RedisProperties? = null

    @org.springframework.context.annotation.Bean
    fun redissonClient(): RedissonClient {
        val config = Config()
        config.useSingleServer()
            .setAddress((SCHEME + redisProperties!!.host).toString() + ":" + redisProperties.port)
            .setPassword(redisProperties.password).timeout = redisProperties.timeout

        return Redisson.create(config)
    }

    companion object {
        private const val SCHEME = "redis://"
    }
}
