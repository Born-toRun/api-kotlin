package kr.kro.btr.support.oauth.entity

import kr.kro.btr.domain.constant.ProviderType
import kr.kro.btr.domain.constant.RoleType
import kr.kro.btr.domain.entity.UserEntity
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.oauth2.core.oidc.OidcIdToken
import org.springframework.security.oauth2.core.oidc.OidcUserInfo
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.security.oauth2.core.user.OAuth2User

class UserPrincipal(
    val userId: Long,
    val name: String?,
    private val password: String?,
    val providerType: ProviderType?,
    val roleType: RoleType,
    val authorities: Collection<GrantedAuthority>
) : OAuth2User, UserDetails, OidcUser {

    var attributes: Map<String, Any>? = null

    companion object {
        fun create(user: UserEntity): UserPrincipal {
            return UserPrincipal(
                user.id,
                user.name,
                user.socialId,
                user.providerType,
                user.roleType,
                listOf(SimpleGrantedAuthority(user.roleType.code))
            )
        }

        fun create(user: UserEntity, attributes: Map<String, Any>): UserPrincipal {
            val userPrincipal = create(user)
            userPrincipal.attributes = attributes
            return userPrincipal
        }
    }

    override fun getAttributes(): Map<String, Any>? {
        return attributes
    }

    override fun getUsername(): String {
        return userId.toString()
    }

    override fun getPassword(): String? {
        return password
    }

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true

    override fun getAuthorities(): Collection<GrantedAuthority> = authorities

    override fun getClaims(): Map<String, Any>? = null

    override fun getUserInfo(): OidcUserInfo? = null

    override fun getIdToken(): OidcIdToken? = null

    override fun getName(): String? {
        return name
    }
}