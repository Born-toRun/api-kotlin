package kr.kro.btr.support.oauth.repository

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kr.kro.btr.support.CookieSupport
import org.apache.commons.lang3.StringUtils
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.REDIRECT_URI
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.REFRESH_TOKEN
import org.springframework.stereotype.Component

@Component
class OAuth2AuthorizationRequestBasedOnCookieRepository :
    AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    override fun loadAuthorizationRequest(request: HttpServletRequest): OAuth2AuthorizationRequest? {
        return CookieSupport.getCookie(request, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)
            ?.let { CookieSupport.deserialize(it, OAuth2AuthorizationRequest::class.java) }

    }

    override fun saveAuthorizationRequest(
        authorizationRequest: OAuth2AuthorizationRequest?,
        request: HttpServletRequest,
        response: HttpServletResponse
    ) {
        if (authorizationRequest == null) {
            CookieSupport.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)
            CookieSupport.deleteCookie(request, response, REDIRECT_URI)
            CookieSupport.deleteCookie(request, response, REFRESH_TOKEN)
            return
        }

        CookieSupport.addCookie(
            response,
            OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME,
            CookieSupport.serialize(authorizationRequest),
            COOKIE_EXPIRE_SECONDS
        )

        val redirectUriAfterLogin = request.getParameter(REDIRECT_URI)
        if (StringUtils.isNotBlank(redirectUriAfterLogin)) {
            CookieSupport.addCookie(response, REDIRECT_URI, redirectUriAfterLogin, COOKIE_EXPIRE_SECONDS)
        }
    }

    override fun removeAuthorizationRequest(
        request: HttpServletRequest,
        response: HttpServletResponse
    ): OAuth2AuthorizationRequest? {
        return loadAuthorizationRequest(request)
    }

    fun removeAuthorizationRequestCookies(request: HttpServletRequest, response: HttpServletResponse) {
        CookieSupport.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)
        CookieSupport.deleteCookie(request, response, REDIRECT_URI)
        CookieSupport.deleteCookie(request, response, REFRESH_TOKEN)
    }

    companion object {
        private const val OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME = "oauth2_auth_request"
        private const val COOKIE_EXPIRE_SECONDS = 180
    }
}

