package kr.kro.btr.support

import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpHeaders

object HeaderSupport {
    private const val TOKEN_PREFIX = "Bearer "

    fun getAccessToken(request: HttpServletRequest): String? {
        val headerValue = request.getHeader(HttpHeaders.AUTHORIZATION) ?: return null
        return if (headerValue.startsWith(TOKEN_PREFIX)) {
            headerValue.substring(TOKEN_PREFIX.length)
        } else {
            null
        }
    }
}
