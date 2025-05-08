package kr.kro.btr.support.oauth.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kr.kro.btr.support.HeaderSupport
import kr.kro.btr.support.oauth.token.AuthTokenProvider
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException

class TokenAuthenticationFilter(
    private val tokenProvider: AuthTokenProvider
) : OncePerRequestFilter() {

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token = HeaderSupport.getAccessToken(request)

        if (token != null) {
            val authToken = tokenProvider.convertAuthToken(token)

            if (authToken.isValidate()) {
                val authentication = tokenProvider.getAuthentication(authToken)
                SecurityContextHolder.getContext().authentication = authentication
            }
        }

        filterChain.doFilter(request, response)
    }
}
