package kr.kro.btr.domain.port

import kr.kro.btr.domain.entity.UserRefreshTokenEntity
import kr.kro.btr.domain.port.model.CreateRefreshTokenCommand


interface UserRefreshTokenPort {
    fun searchByUserId(userId: Long): UserRefreshTokenEntity?
    fun create(command: CreateRefreshTokenCommand): UserRefreshTokenEntity
}
