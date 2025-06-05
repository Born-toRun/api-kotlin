package kr.kro.btr.adapter.out.thirdparty

import club.minnced.discord.webhook.WebhookClient
import club.minnced.discord.webhook.send.WebhookEmbedBuilder
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class DiscordAdapter(
    private val discordConnector: WebhookClient
) {

    @Async
    fun send(message: String) {
        if (message.isNotBlank()) {
            log.debug { "discord message: $message" }

            sliceMessage(message).forEach { part ->
                sendMessageToDiscord(part)
            }
        }
    }

    private fun sliceMessage(message: String): List<String> {
        val slicedMessage = mutableListOf<String>()
        var index = 0

        while (index < message.length) {
            var length = minOf(MESSAGE_MAX_SIZE, message.length - index)
            val nextIndex = message.indexOf("\n", index)

            if (nextIndex != -1 && nextIndex == index + 1 && length > 10) {
                length = MESSAGE_MAX_SIZE - 1
            }

            slicedMessage.add(message.substring(index, index + length).trim())
            index += length
        }

        return slicedMessage.filter { it.isNotEmpty() }
    }

    private fun sendMessageToDiscord(message: String) {
        try {
            discordConnector.send(message).thenAccept { response ->
                log.info { "discord 알림이 발송 되었습니다. [${response.id}]" }
            }
        } catch (e: Exception) {
            log.error(e) { "Discord notify failed!" }
        }
    }

    @Async
    fun send(webhookEmbedBuilder: WebhookEmbedBuilder) {
        val embed = webhookEmbedBuilder.setColor(0xFF006E).build()
        log.debug { "discord message: ${embed.description}" }

        try {
            discordConnector.send(embed).thenAccept { message ->
                log.info { "discord 알림이 발송 되었습니다. [${message.id}]" }
            }
        } catch (e: Exception) {
            log.error(e) { "Discord notify failed!" }
        }
    }

    companion object {
        private const val MESSAGE_MAX_SIZE = 2000
        private val log = KotlinLogging.logger {}
    }
}
