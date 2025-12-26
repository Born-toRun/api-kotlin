package kr.kro.btr.core.service

import kr.kro.btr.base.extension.toCreateRefreshTokenQuery
import kr.kro.btr.domain.entity.UserRefreshTokenEntity
import kr.kro.btr.domain.port.UserRefreshTokenPort
import kr.kro.btr.domain.port.model.CreateRefreshTokenCommand
import kr.kro.btr.infrastructure.UserRefreshTokenGateway
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserRefreshTokenService(
    private val userRefreshTokenGateway: UserRefreshTokenGateway
) : UserRefreshTokenPort {

    @Transactional
    override fun create(command: CreateRefreshTokenCommand): UserRefreshTokenEntity {
        val query = command.toCreateRefreshTokenQuery()
        return userRefreshTokenGateway.save(query)
    }
}
