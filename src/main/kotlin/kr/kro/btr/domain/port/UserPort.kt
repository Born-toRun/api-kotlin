package kr.kro.btr.domain.port

import kr.kro.btr.domain.port.model.CreateUserCommand
import kr.kro.btr.domain.port.model.ModifyUserCommand
import kr.kro.btr.domain.port.model.SignUpCommand
import kr.kro.btr.domain.port.model.result.BornToRunUser

interface UserPort {
    fun signUp(command: SignUpCommand): String
    fun refreshToken(accessToken: String, refreshToken: String): String
    fun remove(userId: Long)
    fun searchById(userId: Long): BornToRunUser
    fun searchBySocialId(socialId: String): BornToRunUser
    fun modify(command: ModifyUserCommand): BornToRunUser
    fun createAndFlush(command: CreateUserCommand): BornToRunUser
    fun exists(socialId: String): Boolean
}
