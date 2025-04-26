package kr.kro.btr.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "cors")
data class CorsProperties (
    val allowedOrigins: String,
    val allowedMethods: String,
    val allowedHeaders: String,
    val maxAge: Long
)
