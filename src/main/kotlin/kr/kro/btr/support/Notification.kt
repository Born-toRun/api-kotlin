package kr.kro.btr.support

import club.minnced.discord.webhook.send.WebhookEmbed
import club.minnced.discord.webhook.send.WebhookEmbed.EmbedField
import club.minnced.discord.webhook.send.WebhookEmbed.EmbedTitle
import club.minnced.discord.webhook.send.WebhookEmbedBuilder
import kr.kro.btr.adapter.out.thirdparty.DiscordAdapter
import org.springframework.stereotype.Component

@Component
class Notification(
    private val discordAdapter: DiscordAdapter
) {
    fun send(message: String) {
        discordAdapter.send(message)
    }

    fun send(title: String, description: String, embedFieldMap: Map<String, String>) {
        val embedFields = embedFieldMap.map { (key, value) ->
            EmbedField(true, key, value.truncate())
        }

        val webhookEmbed = WebhookEmbed(null, null, description.truncate(), null, null, null, EmbedTitle(title, null), null, embedFields)
        discordAdapter.send(WebhookEmbedBuilder(webhookEmbed))
    }

    companion object {
        private const val EMBED_DESCRIPTION_MAX_SIZE = 4096

        private fun String.truncate(): String {
            return if (length > EMBED_DESCRIPTION_MAX_SIZE) {
                substring(0, EMBED_DESCRIPTION_MAX_SIZE)
            } else {
                this
            }
        }
    }
}
