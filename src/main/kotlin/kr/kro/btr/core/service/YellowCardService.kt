package kr.kro.btr.core.service

import kr.kro.btr.core.converter.YellowCardConverter
import kr.kro.btr.domain.port.YellowCardPort
import kr.kro.btr.domain.port.model.CreateYellowCardCommand
import kr.kro.btr.infrastructure.UserGateway
import kr.kro.btr.infrastructure.YellowCardGateway
import kr.kro.btr.support.exception.DuplicationException
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
        val isExists = yellowCardGateway.exists(command.sourceUserId, command.targetUserId)
        if (isExists) {
            throw DuplicationException(
                "이미 신고한 사용자입니다. [{${command.sourceUserId} to ${command.targetUserId}]}"
            )
        }

        val sourceUser = userGateway.searchById(command.sourceUserId)
        val targetUser = userGateway.searchById(command.targetUserId)

        val query = yellowCardConverter.map(command, sourceUser, targetUser)
        yellowCardGateway.create(query)
    }
}
