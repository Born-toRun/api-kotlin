package kr.kro.btr.core.service

import kr.kro.btr.core.converter.YellowCardConverter
import kr.kro.btr.domain.port.YellowCardPort
import kr.kro.btr.domain.port.model.CreateYellowCardCommand
import kr.kro.btr.infrastructure.UserGateway
import kr.kro.btr.infrastructure.YellowCardGateway
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class YellowCardService(
    private val yellowCardConverter: YellowCardConverter,
    private val yellowCardGateway: YellowCardGateway,
    private val userGateway: UserGateway
) : YellowCardPort {

    @Transactional
    override fun create(command: CreateYellowCardCommand) {
        val query = yellowCardConverter.map(command)
        yellowCardGateway.create(query)
    }
}
