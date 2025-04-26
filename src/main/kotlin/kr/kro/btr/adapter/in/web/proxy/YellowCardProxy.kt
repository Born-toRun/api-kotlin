package kr.kro.btr.adapter.`in`.web.proxy

import kr.kro.btr.adapter.`in`.web.payload.CreateYellowCardRequest
import kr.kro.btr.core.converter.YellowCardConverter
import kr.kro.btr.domain.port.YellowCardPort
import org.springframework.stereotype.Component

@Component
class YellowCardProxy(
    private val yellowCardConverter: YellowCardConverter,
    private val yellowCardPort: YellowCardPort
) {

    fun create(myUserId: Long, request: CreateYellowCardRequest) {
        val command = yellowCardConverter.map(request, myUserId)
        yellowCardPort.create(command)
    }
}
