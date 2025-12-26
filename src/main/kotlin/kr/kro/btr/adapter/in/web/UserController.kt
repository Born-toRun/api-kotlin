package kr.kro.btr.adapter.`in`.web

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import kr.kro.btr.adapter.`in`.web.payload.*
import kr.kro.btr.adapter.`in`.web.proxy.UserProxy
import kr.kro.btr.base.extension.toModifyUserResponse
import kr.kro.btr.base.extension.toUserDetailResponse
import kr.kro.btr.support.CookieSupport
import kr.kro.btr.support.TokenDetail
import kr.kro.btr.support.annotation.AuthUser
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.REFRESH_TOKEN
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val userProxy: UserProxy
) {

    @PostMapping("/refresh", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun refresh(
        @RequestHeader(HttpHeaders.AUTHORIZATION) authorizationHeader: String,
        @CookieValue(REFRESH_TOKEN) refreshToken: String,
        request: HttpServletRequest,
        response: HttpServletResponse
    ): ResponseEntity<RefreshTokenResponse> {
        val accessToken = authorizationHeader.removePrefix("Bearer ").trim()
        require(accessToken.isNotBlank()) { "Access token must not be blank" }

        val result = userProxy.refreshToken(accessToken, refreshToken)

        CookieSupport.deleteCookie(request, response, REFRESH_TOKEN)
        CookieSupport.addCookie(request, response, REFRESH_TOKEN, result.refreshToken, result.cookieMaxAge)

        return ResponseEntity.ok(RefreshTokenResponse(result.accessToken, result.refreshToken))
    }

    @PutMapping("/sign-up", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun signUp(@AuthUser my: TokenDetail, @RequestBody @Valid request: SignUpRequest): ResponseEntity<SignUpResponse> {
        val createdUserName = userProxy.signUp(my, request)
        return ResponseEntity.ok(SignUpResponse(createdUserName))
    }

    @DeleteMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun remove(@AuthUser my: TokenDetail): ResponseEntity<Void> {
        userProxy.remove(my.id)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/my", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun detail(@AuthUser my: TokenDetail): ResponseEntity<DetailUserResponse> {
        val user = userProxy.search(my)
        val response = user.toUserDetailResponse()
        return ResponseEntity.ok(response)
    }

    @GetMapping("/{userId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun detail(@PathVariable userId: Long): ResponseEntity<DetailUserResponse> {
        val user = userProxy.search(userId)
        val response = user.toUserDetailResponse()
        return ResponseEntity.ok(response)
    }

    @PutMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun modify(@AuthUser my: TokenDetail, @RequestBody @Valid request: ModifyUserRequest): ResponseEntity<ModifyUserResponse> {
        val modifiedUser = userProxy.modify(my, request)
        val response = modifiedUser.toModifyUserResponse()
        return ResponseEntity.ok(response)
    }
}
