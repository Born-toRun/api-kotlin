package kr.kro.btr.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding

@ConfigurationProperties("adapter.discord")
data class DiscordProperties @ConstructorBinding constructor (
    val host: String,
    val webhookPath: String,
    val contentType: String
)
