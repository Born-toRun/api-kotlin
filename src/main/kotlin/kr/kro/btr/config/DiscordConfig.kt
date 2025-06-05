package kr.kro.btr.config

import club.minnced.discord.webhook.WebhookClient
import kr.kro.btr.config.properties.DiscordProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(DiscordProperties::class)
class DiscordConfig(
    private val discordProperties: DiscordProperties,
) {

    @Bean
    fun discordConnector(): WebhookClient {
        return WebhookClient.withUrl(discordProperties.host + discordProperties.webhookPath)
    }
}
