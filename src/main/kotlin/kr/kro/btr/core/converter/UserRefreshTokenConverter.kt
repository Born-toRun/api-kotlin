package kr.kro.btr.core.converter

import kr.kro.btr.domain.entity.UserEntity
import kr.kro.btr.domain.port.model.CreateRefreshTokenCommand
import kr.kro.btr.infrastructure.model.CreateRefreshTokenQuery
import org.springframework.stereotype.Component

@Component
class UserRefreshTokenConverter {

    fun map(source: CreateRefreshTokenCommand, userEntity: UserEntity): CreateRefreshTokenQuery {
        return CreateRefreshTokenQuery(
            userId = source.userId,
            refreshToken = source.refreshToken,
            userEntity = userEntity,
        )
    }
}
