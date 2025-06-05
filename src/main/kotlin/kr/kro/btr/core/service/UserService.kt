package kr.kro.btr.core.service

import kr.kro.btr.base.extension.toBornToRunUser
import kr.kro.btr.base.extension.toCreateUserQuery
import kr.kro.btr.base.extension.toModifyUserQuery
import kr.kro.btr.base.extension.toSignUpUserQuery
import kr.kro.btr.domain.port.UserPort
import kr.kro.btr.domain.port.model.result.BornToRunUser
import kr.kro.btr.domain.port.model.CreateUserCommand
import kr.kro.btr.domain.port.model.ModifyUserCommand
import kr.kro.btr.domain.port.model.SignUpCommand
import kr.kro.btr.infrastructure.UserGateway
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userGateway: UserGateway
) : UserPort {

    @Transactional
    override fun signUp(command: SignUpCommand): String {
        val query = command.toSignUpUserQuery()
        return userGateway.signUp(query)
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
