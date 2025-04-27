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
    private val userName: String?,  // 변경: 'name' -> 'userName'
    private val password: String?,
    val providerType: ProviderType?,
    val roleType: RoleType,
    private val grantedAuthorities: Collection<GrantedAuthority> // 변경: 'authorities' -> 'grantedAuthorities'
) : OAuth2User, UserDetails, OidcUser {

    private var attributeMap: Map<String, Any>? = null  // 변경: 'attributes' -> 'attributeMap'

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
            userPrincipal.attributeMap = attributes
            return userPrincipal
        }
    }

    override fun getAttributes(): Map<String, Any>? {
        return attributeMap
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

    override fun getAuthorities(): Collection<GrantedAuthority> = grantedAuthorities
    override fun getClaims(): Map<String, Any>? = null
    override fun getUserInfo(): OidcUserInfo? = null
    override fun getIdToken(): OidcIdToken? = null

    override fun getName(): String? {
        return userName
    }
}
