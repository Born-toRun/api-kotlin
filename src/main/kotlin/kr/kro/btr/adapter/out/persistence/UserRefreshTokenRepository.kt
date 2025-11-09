package kr.kro.btr.adapter.out.persistence

import kr.kro.btr.domain.entity.UserRefreshTokenEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserRefreshTokenRepository : JpaRepository<UserRefreshTokenEntity, Long> {

    fun findByUserId(userId: Long): UserRefreshTokenEntity?
}
