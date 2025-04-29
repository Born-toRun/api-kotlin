package kr.kro.btr.common.security

import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.FilterConfig
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import kr.kro.btr.domain.constant.ProviderType
import kr.kro.btr.domain.constant.RoleType
import kr.kro.btr.domain.port.model.BornToRunUser
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import java.time.LocalDateTime

class MockSecurityFilter : Filter {
	override fun init(filterConfig: FilterConfig) {}

	override fun doFilter(
		request: ServletRequest,
		response: ServletResponse?,
		chain: FilterChain
	) {
		val principalUser = createMember()
		SecurityContextHolder.getContext().authentication =
			UsernamePasswordAuthenticationToken(principalUser, "", mutableListOf(SimpleGrantedAuthority(principalUser.roleType.toString())))

		chain.doFilter(request, response)
	}

	override fun destroy() {
		SecurityContextHolder.clearContext()
	}

	fun getFilters(mockHttpServletRequest: MockHttpServletRequest) {}

	companion object {
		private fun createMember(): BornToRunUser {
			return BornToRunUser(
                userId = 1,
                socialId = "socialId",
                providerType = ProviderType.KAKAO,
                refreshToken = "refreshToken",
                roleType = RoleType.ADMIN,
                userName = "userName",
                crewId = 1,
                crewName = "crewName",
                instagramId = "instagramId",
                profileImageUri = "profileImageUri",
                lastLoginAt = LocalDateTime.now(),
                isAdmin = true,
                isManager = false,
                yellowCardQty = 1,
                isInstagramIdPublic = false
            )
		}
	}
}
