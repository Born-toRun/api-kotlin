package kr.kro.btr.infrastructure

import kr.kro.btr.adapter.out.persistence.UserRefreshTokenRepository
import kr.kro.btr.domain.entity.UserRefreshTokenEntity
import kr.kro.btr.infrastructure.model.CreateRefreshTokenQuery
import org.springframework.stereotype.Component

@Component
class UserRefreshTokenGateway(
    private val userRefreshTokenRepository: UserRefreshTokenRepository
) {

    fun searchByUserId(userId: Long): UserRefreshTokenEntity? {
        return userRefreshTokenRepository.findByUserId(userId)
    }

    fun saveAndFlush(query: CreateRefreshTokenQuery): UserRefreshTokenEntity {
        val userRefreshTokenEntity = UserRefreshTokenEntity(
            userId = query.userId,
            refreshToken = query.refreshToken
        )

        userRefreshTokenEntity.add(query.userEntity)

        return userRefreshTokenRepository.saveAndFlush(userRefreshTokenEntity)
    }
}
