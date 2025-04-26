package kr.kro.btr.support.oauth

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint

class RestAuthenticationEntryPoint : AuthenticationEntryPoint {

    companion object {
        private val log = KotlinLogging.logger {}
    }

    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        log.info { "[$request.method.uppercase() $request.requestURI] Responding with unauthorized error: $authException.message" }

        response.sendError(
            HttpServletResponse.SC_UNAUTHORIZED,
            authException.localizedMessage
        )
    }
}