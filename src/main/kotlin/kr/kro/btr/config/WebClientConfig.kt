package kr.kro.btr.config

import kr.kro.btr.config.properties.DiscordProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
@EnableConfigurationProperties(DiscordProperties::class)
class WebClientConfig(
    private val discordProperties: DiscordProperties,
    private val webClientBuilder: WebClient.Builder
) {

    @org.springframework.context.annotation.Bean
    fun discordConnector(): WebClient {
        return webClientBuilder.baseUrl(discordProperties.host)
            .defaultHeader(org.springframework.http.HttpHeaders.CONTENT_TYPE, discordProperties.contentType)
            .build()
    }
}
