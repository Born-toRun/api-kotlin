package kr.kro.btr.core.service

import kr.kro.btr.base.extension.toBornToRunUser
import kr.kro.btr.base.extension.toCreateUserQuery
import kr.kro.btr.base.extension.toModifyUserQuery
import kr.kro.btr.base.extension.toSignUpUserQuery
import kr.kro.btr.config.properties.AppProperties
import kr.kro.btr.domain.port.UserPort
import kr.kro.btr.domain.port.model.CreateUserCommand
import kr.kro.btr.domain.port.model.ModifyUserCommand
import kr.kro.btr.domain.port.model.SignUpCommand
import kr.kro.btr.domain.port.model.result.BornToRunUser
import kr.kro.btr.infrastructure.UserGateway
import kr.kro.btr.infrastructure.UserRefreshTokenGateway
import kr.kro.btr.support.exception.InvalidTokenException
import kr.kro.btr.support.oauth.token.AuthTokenProvider
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class UserService(
    private val userGateway: UserGateway,
    private val userRefreshTokenGateway: UserRefreshTokenGateway,
    private val tokenProvider: AuthTokenProvider,
    private val appProperties: AppProperties
) : UserPort {

    @Transactional
    override fun signUp(command: SignUpCommand): String {
        val query = command.toSignUpUserQuery()
        return userGateway.signUp(query)
    }

    @Transactional
    override fun refreshToken(accessToken: String, refreshToken: String): String {
        val authToken = tokenProvider.convertAuthToken(accessToken)
        val claims = authToken.getExpiredTokenClaims()
            ?: throw InvalidTokenException("다시 로그인해주세요.")

        val userId = claims.subject.toLong()
        val user = userGateway.searchById(userId)

        val refreshTokenEntity = userRefreshTokenGateway.searchByUserId(userId)
            ?: throw InvalidTokenException("다시 로그인해주세요.")

        if (refreshTokenEntity.refreshToken != refreshToken) {
            throw InvalidTokenException("다시 로그인해주세요.")
        }

        val refreshTokenAuthToken = tokenProvider.convertAuthToken(refreshToken)
        try {
            refreshTokenAuthToken.validate()
        } catch (e: InvalidTokenException) {
            throw InvalidTokenException("다시 로그인해주세요.")
        }

        val now = Date()
        val newAccessToken = tokenProvider.createAuthToken(
            user.id,
            user.name,
            user.crewId,
            user.roleType.code,
            Date(now.time + appProperties.auth.tokenExpiry)
        )

        return newAccessToken.token
    }

    @Transactional
    override fun remove(userId: Long) {
        userGateway.remove(userId)
    }

    @Transactional(readOnly = true)
    override fun searchById(userId: Long): BornToRunUser {
        val userEntity = userGateway.searchById(userId)
        return userEntity.toBornToRunUser()
    }

    @Transactional(readOnly = true)
    override fun searchBySocialId(socialId: String): BornToRunUser {
        val userEntity = userGateway.searchBySocialId(socialId)
        return userEntity.toBornToRunUser()
    }

    @Transactional
    override fun modify(command: ModifyUserCommand): BornToRunUser {
        val query = command.toModifyUserQuery()
        val modifiedUser = userGateway.modify(query)
        return modifiedUser.toBornToRunUser()
    }

    @Transactional
    override fun createAndFlush(command: CreateUserCommand): BornToRunUser {
        val query = command.toCreateUserQuery()
        val guest = userGateway.createAndFlush(query)
        return guest.toBornToRunUser()
    }

    @Transactional(readOnly = true)
    override fun exists(socialId: String): Boolean {
        return userGateway.exists(socialId)
    }
}
