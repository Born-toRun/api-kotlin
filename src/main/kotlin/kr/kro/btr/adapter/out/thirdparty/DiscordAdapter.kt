package kr.kro.btr.adapter.out.thirdparty

import io.github.oshai.kotlinlogging.KotlinLogging
import kr.kro.btr.config.properties.DiscordProperties
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import java.time.Duration

@Component
class DiscordAdapter(
    private val discordProperties: DiscordProperties,
    private val discordConnector: WebClient
) {
    companion object {
        private const val MESSAGE_MAX_SIZE = 2000
        private const val MESSAGE = "content"
        private val log = KotlinLogging.logger {}
    }

    @Async
    fun send(message: String?) {
        if (message.isNullOrEmpty()) return

        log.info { "discord error message: $message" }

        val slicedMessage = mutableListOf<String>()
        var index = 0

        while (index < message.length) {
            var length = minOf(MESSAGE_MAX_SIZE, message.length - index)
            val nextIndex = message.indexOf("\n", index)

            if (nextIndex != -1 && nextIndex == index + 1 && length > 10) {
                length = MESSAGE_MAX_SIZE - 1
            }

            slicedMessage.add(message.substring(index, index + length))
            index += length
        }

        try {
            for (m in slicedMessage.map { it.trim() }.filter { it.isNotEmpty() }) {
                val body: MultiValueMap<String, String> = LinkedMultiValueMap()
                body.add(MESSAGE, m)

                discordConnector.post()
                    .uri { uriBuilder -> uriBuilder.path(discordProperties.webhookPath).build() }
                    .body(BodyInserters.fromFormData(body))
                    .retrieve()
                    .toEntity(Void::class.java)
                    .delayElement(Duration.ofSeconds(1))
                    .block()
            }
        } catch (e: Exception) {
            log.error(e) { "Discord notify failed" }
        }
    }
}
