package kr.kro.btr.core.service

import kr.kro.btr.base.extension.toCreateYellowCardQuery
import kr.kro.btr.domain.port.YellowCardPort
import kr.kro.btr.domain.port.model.CreateYellowCardCommand
import kr.kro.btr.infrastructure.YellowCardGateway
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class YellowCardService(
    private val yellowCardGateway: YellowCardGateway,
) : YellowCardPort {

    @Transactional
    override fun create(command: CreateYellowCardCommand) {
        val query = command.toCreateYellowCardQuery()
        yellowCardGateway.create(query)
    }
}
