package kr.kro.btr.adapter.`in`.web.proxy

import kr.kro.btr.adapter.`in`.web.payload.CreateYellowCardRequest
import kr.kro.btr.base.extension.toCreateYellowCardCommand
import kr.kro.btr.domain.port.YellowCardPort
import org.springframework.stereotype.Component

@Component
class YellowCardProxy(
    private val yellowCardPort: YellowCardPort
) {

    fun create(myUserId: Long, request: CreateYellowCardRequest) {
        val command = request.toCreateYellowCardCommand(myUserId)
        yellowCardPort.create(command)
    }
}
