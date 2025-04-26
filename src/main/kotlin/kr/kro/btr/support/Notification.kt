package kr.kro.btr.support

import kr.kro.btr.adapter.out.thirdparty.DiscordAdapter
import org.springframework.stereotype.Component

@Component
class Notification(
    private val discordAdapter: DiscordAdapter
) {
    fun send(message: String) {
        discordAdapter.send(message)
    }
}
