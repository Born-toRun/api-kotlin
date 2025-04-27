package kr.kro.btr.core.service

import kr.kro.btr.core.converter.UserRefreshTokenConverter
import kr.kro.btr.domain.entity.UserRefreshTokenEntity
import kr.kro.btr.domain.port.UserRefreshTokenPort
import kr.kro.btr.domain.port.model.CreateRefreshTokenCommand
import kr.kro.btr.infrastructure.UserGateway
import kr.kro.btr.infrastructure.UserRefreshTokenGateway
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserRefreshTokenService(
    private val userRefreshTokenGateway: UserRefreshTokenGateway,
    private val userGateway: UserGateway,
    private val userRefreshTokenConverter: UserRefreshTokenConverter
) : UserRefreshTokenPort {

    @Transactional(readOnly = true)
    override fun searchByUserId(userId: Long): UserRefreshTokenEntity? {
        return userRefreshTokenGateway.searchByUserId(userId)
    }

    @Transactional
    override fun createAndFlush(command: CreateRefreshTokenCommand): UserRefreshTokenEntity {
        val userEntity = userGateway.searchById(command.userId)
        val query = userRefreshTokenConverter.map(command, userEntity)
        return userRefreshTokenGateway.saveAndFlush(query)
    }
}
