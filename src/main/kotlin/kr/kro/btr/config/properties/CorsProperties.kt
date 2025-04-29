package kr.kro.btr.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding

@ConfigurationProperties(prefix = "cors")
data class CorsProperties @ConstructorBinding constructor (
    val allowedOrigins: String,
    val allowedMethods: String,
    val allowedHeaders: String,
    val maxAge: Long
)
