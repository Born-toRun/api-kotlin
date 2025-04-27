package kr.kro.btr.support.oauth.service

import kr.kro.btr.infrastructure.UserGateway

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class BornToRunUserDetailsService(
    private val userGateway: UserGateway
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val userEntity = userGateway.searchBySocialId(username)
        return UserPrincipal.create(userEntity)
    }
}

