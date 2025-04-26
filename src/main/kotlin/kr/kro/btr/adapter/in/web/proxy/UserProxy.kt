package kr.kro.btr.adapter.`in`.web.proxy

import kr.kro.btr.adapter.`in`.web.payload.ModifyUserRequest
import kr.kro.btr.adapter.`in`.web.payload.SignUpRequest
import kr.kro.btr.core.converter.UserConverter
import kr.kro.btr.domain.port.UserPort
import kr.kro.btr.domain.port.model.BornToRunUser
import kr.kro.btr.domain.port.model.RefreshTokenResult
import kr.kro.btr.support.TokenDetail
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component

@Component
@CacheConfig(cacheNames = ["user"])
class UserProxy(
    private val userConverter: UserConverter,
    private val userPort: UserPort
) {

    @CacheEvict(allEntries = true)
    fun signUp(my: TokenDetail, request: SignUpRequest): String {
        val command = userConverter.map(request, my.id)
        return userPort.signUp(command)
    }

    fun refreshToken(accessToken: String): RefreshTokenResult {
        val refreshedToken = userPort.getRefreshToken(accessToken)
        return RefreshTokenResult(refreshedToken)
    }

    @CacheEvict(allEntries = true)
    fun remove(myUserId: Long) {
        userPort.remove(myUserId)
    }

    @Cacheable(key = "'search: ' + #my.hashCode()")
    fun search(my: TokenDetail): BornToRunUser {
        return userPort.searchById(my.id)
    }

    @Cacheable(key = "'search: ' + #userId")
    fun search(userId: Long): BornToRunUser {
        return userPort.searchById(userId)
    }

    @CacheEvict(allEntries = true)
    fun modify(my: TokenDetail, request: ModifyUserRequest): BornToRunUser {
        val command = userConverter.map(request, my.id)
        return userPort.modify(command)
    }
}
