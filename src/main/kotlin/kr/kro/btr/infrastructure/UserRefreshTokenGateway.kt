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

    fun save(query: CreateRefreshTokenQuery): UserRefreshTokenEntity {
        val userRefreshTokenEntity = searchByUserId(query.userId)?.apply {
            refreshToken = query.refreshToken
        } ?: UserRefreshTokenEntity(
            userId = query.userId,
            refreshToken = query.refreshToken
        )

        return userRefreshTokenRepository.save(userRefreshTokenEntity)
    }
}
