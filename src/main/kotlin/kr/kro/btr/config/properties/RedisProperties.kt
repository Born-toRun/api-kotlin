package kr.kro.btr.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("adapter.redis")
class RedisProperties (
    val host: String,
    val port: Int,
    val password: String,
    val timeout: Int
)