package kr.kro.btr.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding

@ConfigurationProperties("adapter.redis")
data class RedisProperties @ConstructorBinding constructor (
    val host: String,
    val port: Int,
    val password: String,
    val timeout: Int
)
