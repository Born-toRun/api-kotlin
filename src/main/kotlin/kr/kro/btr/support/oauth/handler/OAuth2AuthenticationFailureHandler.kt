package kr.kro.btr.support.oauth.handler

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kr.kro.btr.support.CookieSupport
import kr.kro.btr.support.oauth.repository.OAuth2AuthorizationRequestBasedOnCookieRepository
import org.springframework.security.core.AuthenticationException
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.REDIRECT_URI
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder

@Component
class OAuth2AuthenticationFailureHandler(
    private val authorizationRequestRepository: OAuth2AuthorizationRequestBasedOnCookieRepository
) : SimpleUrlAuthenticationFailureHandler() {

    override fun onAuthenticationFailure(
        request: HttpServletRequest,
        response: HttpServletResponse,
        exception: AuthenticationException
    ) {
        val baseUrl = CookieSupport.getCookie(request, REDIRECT_URI)?.value ?: "/"
        val targetUrl = UriComponentsBuilder.fromUriString(baseUrl)
            .queryParam("error", exception.localizedMessage)
            .build()
            .toUriString()


        authorizationRequestRepository.removeAuthorizationRequestCookies(request, response)

        // TODO: 인증 실패시 회원가입으로 이동하는 건 말이 안됨. 홈으로 이동?
        redirectStrategy.sendRedirect(request, response, targetUrl)

        log.info { "Authentication failed! $exception.message" }
    }

    companion object {
        private val log = KotlinLogging.logger {}
    }
}
