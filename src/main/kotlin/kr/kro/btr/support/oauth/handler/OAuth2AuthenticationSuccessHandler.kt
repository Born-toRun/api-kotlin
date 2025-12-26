package kr.kro.btr.support.oauth.handler

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kr.kro.btr.config.properties.AppProperties
import kr.kro.btr.domain.constant.ProviderType
import kr.kro.btr.domain.constant.RoleType
import kr.kro.btr.domain.port.UserPort
import kr.kro.btr.domain.port.UserRefreshTokenPort
import kr.kro.btr.domain.port.model.CreateRefreshTokenCommand
import kr.kro.btr.domain.port.model.CreateUserCommand
import kr.kro.btr.domain.port.model.result.BornToRunUser
import kr.kro.btr.support.CookieSupport
import kr.kro.btr.support.exception.InternalServerException
import kr.kro.btr.support.oauth.info.OAuth2UserInfoFactory
import kr.kro.btr.support.oauth.repository.OAuth2AuthorizationRequestBasedOnCookieRepository
import kr.kro.btr.support.oauth.token.AuthTokenProvider
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.REDIRECT_URI
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.REFRESH_TOKEN
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI
import java.util.*

@Component
class OAuth2AuthenticationSuccessHandler(
    private val tokenProvider: AuthTokenProvider,
    private val appProperties: AppProperties,
    private val userPort: UserPort,
    private val userRefreshTokenPort: UserRefreshTokenPort,
    private val authorizationRequestRepository: OAuth2AuthorizationRequestBasedOnCookieRepository,
    private val passwordEncoder: BCryptPasswordEncoder
) : SimpleUrlAuthenticationSuccessHandler() {

    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        val targetUrl = determineTargetUrl(request, response, authentication)

        if (response.isCommitted) {
            logger.debug("Response has already been committed. Unable to redirect to $targetUrl")
            return
        }

        clearAuthenticationAttributes(request, response)
        redirectStrategy.sendRedirect(request, response, targetUrl)
        logger.info("Authentication succeeded!")
    }

    override fun determineTargetUrl(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ): String {
        val redirectUri = CookieSupport.getCookie(request, REDIRECT_URI)?.value

        if (redirectUri != null && !isAuthorizedRedirectUri(redirectUri)) {
            logger.error("VALIDATION FAILED - Unauthorized redirect URI: $redirectUri")
            throw InternalServerException(
                "Sorry! We've got an Unauthorized Redirect URI and can't proceed with the authentication"
            )
        }

        logger.info("Validation passed. Using redirect URI: ${redirectUri ?: defaultTargetUrl}")

        val targetUrl = redirectUri ?: defaultTargetUrl

        val authToken = authentication as OAuth2AuthenticationToken
        val providerType = ProviderType.valueOf(authToken.authorizedClientRegistrationId.uppercase())

        val user = authentication.principal as OidcUser
        val socialUser = OAuth2UserInfoFactory.getOAuth2UserInfo(providerType, user.attributes)

        val createUserCommand = CreateUserCommand(socialUser.getId(), providerType, RoleType.GUEST)
        val bornToRunUser: BornToRunUser = if (!userPort.exists(socialUser.getId())) {
            userPort.createAndFlush(createUserCommand)
        } else {
            userPort.searchBySocialId(socialUser.getId())
        }

        val now = Date()
        val accessTokenExpiry = appProperties.auth.tokenExpiry
        val accessToken = tokenProvider.createAuthToken(
            bornToRunUser.userId,
            bornToRunUser.userName,
            bornToRunUser.crewId,
            bornToRunUser.roleType.code,
            Date(now.time + accessTokenExpiry)
        )

        val refreshTokenExpiry = appProperties.auth.refreshTokenExpiry
        val refreshToken = tokenProvider.createAuthToken(
            bornToRunUser.userId,
            Date(now.time + refreshTokenExpiry)
        )

        val hashedRefreshToken = passwordEncoder.encode(refreshToken.token)
        val command = CreateRefreshTokenCommand(bornToRunUser.userId, hashedRefreshToken)
        userRefreshTokenPort.create(command)

        val cookieMaxAge = (refreshTokenExpiry / 1000).toInt()

        CookieSupport.deleteCookie(request, response, REFRESH_TOKEN)
        CookieSupport.addCookie(request, response, REFRESH_TOKEN, refreshToken.token, cookieMaxAge)

        return UriComponentsBuilder.fromUriString(targetUrl)
            .queryParam("isMember", bornToRunUser.crewId != null)
            .queryParam("accessToken", accessToken.token)
            .build().toUriString()
    }

    protected fun clearAuthenticationAttributes(request: HttpServletRequest, response: HttpServletResponse) {
        super.clearAuthenticationAttributes(request)
        authorizationRequestRepository.removeAuthorizationRequestCookies(request, response)
    }

    private fun isAuthorizedRedirectUri(uri: String): Boolean {
        val clientRedirectUri = URI.create(uri)
        logger.info("Validating client redirect URI: scheme=${clientRedirectUri.scheme}, host=${clientRedirectUri.host}, port=${clientRedirectUri.port}")

        val result = appProperties.oauth2.authorizedRedirectUris
            .any { authorizedRedirectUri ->
                val authorizedURI = URI.create(authorizedRedirectUri)
                val hostMatches = authorizedURI.host.equals(clientRedirectUri.host, ignoreCase = true)

                val authorizedPort = if (authorizedURI.port == -1) {
                    if (authorizedURI.scheme.equals("https", ignoreCase = true)) 443 else 80
                } else {
                    authorizedURI.port
                }

                val clientPort = if (clientRedirectUri.port == -1) {
                    if (clientRedirectUri.scheme.equals("https", ignoreCase = true)) 443 else 80
                } else {
                    clientRedirectUri.port
                }

                val matches = hostMatches && authorizedPort == clientPort
                logger.info("  Comparing with: ${authorizedURI.host}:$authorizedPort vs ${clientRedirectUri.host}:$clientPort => $matches")

                matches
            }

        logger.info("Final validation result: $result")
        return result
    }
}
