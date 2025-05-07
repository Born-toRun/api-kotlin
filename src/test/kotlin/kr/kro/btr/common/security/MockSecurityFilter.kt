package kr.kro.btr.common.security

import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.FilterConfig
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import kr.kro.btr.support.TokenDetail
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder

class MockSecurityFilter() : Filter {
	override fun init(filterConfig: FilterConfig) {}

	override fun doFilter(
		request: ServletRequest,
		response: ServletResponse?,
		chain: FilterChain
	) {
        val tokenDetail = TokenDetail(id = 1)

        val authentication: Authentication = UsernamePasswordAuthenticationToken(
            tokenDetail,
            null,
            listOf()
        )

        SecurityContextHolder.getContext().authentication = authentication

        chain.doFilter(request, response)
	}

	override fun destroy() {
		SecurityContextHolder.clearContext()
	}

	fun getFilters(mockHttpServletRequest: MockHttpServletRequest) {}

}
