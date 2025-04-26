package kr.kro.btr.core.service

import kr.kro.btr.core.converter.UserConverter
import kr.kro.btr.domain.port.UserPort
import kr.kro.btr.domain.port.model.BornToRunUser
import kr.kro.btr.domain.port.model.CreateUserCommand
import kr.kro.btr.domain.port.model.ModifyUserCommand
import kr.kro.btr.domain.port.model.SignUpCommand
import kr.kro.btr.infrastructure.UserGateway
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userConverter: UserConverter,
    private val userGateway: UserGateway
) : UserPort {

    @Transactional
    override fun signUp(command: SignUpCommand): String {
        val query = userConverter.map(command)
        return userGateway.modify(query)
    }

    @Transactional
    override fun getRefreshToken(accessToken: String): String {
        // TODO
        return ""
    }

    @Transactional
    override fun remove(userId: Long) {
        userGateway.remove(userId)
    }

    @Transactional(readOnly = true)
    override fun searchById(userId: Long): BornToRunUser {
        val userEntity = userGateway.searchById(userId)
        return userConverter.map(userEntity)
    }

    @Transactional(readOnly = true)
    override fun searchBySocialId(socialId: String): BornToRunUser {
        val userEntity = userGateway.searchBySocialId(socialId)
        return userConverter.map(userEntity)
    }

    @Transactional
    override fun modify(command: ModifyUserCommand): BornToRunUser {
        val query = userConverter.map(command)
        val modifiedUser = userGateway.modify(query)
        return userConverter.map(modifiedUser)
    }

    @Transactional
    override fun createAndFlush(command: CreateUserCommand): BornToRunUser {
        val query = userConverter.map(command)
        val guest = userGateway.createAndFlush(query)
        return userConverter.map(guest)
    }

    @Transactional(readOnly = true)
    override fun exists(socialId: String): Boolean {
        return userGateway.exists(socialId)
    }
}
