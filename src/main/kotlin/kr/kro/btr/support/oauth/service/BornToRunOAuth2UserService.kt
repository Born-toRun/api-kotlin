package kr.kro.btr.support.oauth.service

/*
* 로그인시 user loading
*/
import kr.kro.btr.domain.constant.ProviderType
import kr.kro.btr.domain.constant.RoleType
import kr.kro.btr.domain.entity.UserEntity
import kr.kro.btr.infrastructure.UserGateway
import kr.kro.btr.infrastructure.model.CreateUserQuery
import kr.kro.btr.support.oauth.info.OAuth2UserInfo
import kr.kro.btr.support.oauth.info.OAuth2UserInfoFactory
import org.springframework.security.authentication.InternalAuthenticationServiceException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.lang.Exception

@Service
class BornToRunOAuth2UserService(
    private val userGateway: UserGateway
) : DefaultOAuth2UserService() {

    @Transactional
    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
        val user = super.loadUser(userRequest)
        return try {
            process(userRequest, user)
        } catch (ex: AuthenticationException) {
            throw ex
        } catch (ex: Exception) {
            ex.printStackTrace()
            throw InternalAuthenticationServiceException(ex.message, ex.cause)
        }
    }

    private fun process(userRequest: OAuth2UserRequest, user: OAuth2User): OAuth2User {
        val providerType = ProviderType.valueOf(
            userRequest.clientRegistration.registrationId.uppercase()
        )

        val socialUser = OAuth2UserInfoFactory.getOAuth2UserInfo(providerType, user.attributes)
        var userEntity = userGateway.searchBySocialId(socialUser.getId())

        // TODO: null 처리
        if (userEntity == null) {
            userEntity = createGuest(socialUser, providerType)
        }

        return UserPrincipal.create(userEntity, user.attributes)
    }

    private fun createGuest(userInfo: OAuth2UserInfo, providerType: ProviderType): UserEntity {
        val query = CreateUserQuery(userInfo.getId(), providerType, RoleType.GUEST)
        return userGateway.createAndFlush(query)
    }
}

