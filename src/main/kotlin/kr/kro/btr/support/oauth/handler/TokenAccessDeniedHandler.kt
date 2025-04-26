package kr.kro.btr.support.oauth.handler

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerExceptionResolver
import org.springframework.security.access.AccessDeniedException

@Component
class TokenAccessDeniedHandler(
    private val handlerExceptionResolver: HandlerExceptionResolver
) : AccessDeniedHandler {

    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        accessDeniedException: AccessDeniedException
    ) {
        // response.sendError(HttpServletResponse.SC_FORBIDDEN, accessDeniedException.message)
        handlerExceptionResolver.resolveException(request, response, null, accessDeniedException)
    }
}