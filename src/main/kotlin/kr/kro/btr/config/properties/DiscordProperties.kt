package kr.kro.btr.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("adapter.discord")
data class DiscordProperties(
    val host: String,
    val webhookPath: String,
    val contentType: String
)