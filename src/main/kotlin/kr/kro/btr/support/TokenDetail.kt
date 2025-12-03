package kr.kro.btr.support

import kr.kro.btr.domain.constant.RoleType
import kr.kro.btr.support.oauth.token.AuthToken
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken

data class TokenDetail(
    val id: Long,
    val userName: String? = null,
    val authority: String? = null,
    val crewId: Long? = null,
    val managedCrewId: Long? = null,
    val isAdmin: Boolean = false,
    val isManager: Boolean = false
) {
    constructor(token: JwtAuthenticationToken) : this(
        id = token.token.subject.toLong(),
        userName = token.token.getClaimAsString(AuthToken.USER_NAME_KEY),
        authority = token.token.getClaimAsString(AuthToken.AUTHORITIES_KEY),
        crewId = token.token.getClaimAsString(AuthToken.CREW_ID_KEY)?.toLongOrNull(),
        managedCrewId = token.token.getClaimAsString(AuthToken.MANAGED_CREW_ID_KEY)?.toLongOrNull(),
        isAdmin = token.token.getClaimAsString(AuthToken.AUTHORITIES_KEY) == RoleType.ADMIN.code,
        isManager = token.token.getClaimAsString(AuthToken.AUTHORITIES_KEY) == RoleType.MANAGER.code
    )

    fun isLogin(): Boolean = id > 0

    companion object {
        fun defaultUser(): TokenDetail = TokenDetail(id = -1, authority = RoleType.GUEST.code)
    }
}
