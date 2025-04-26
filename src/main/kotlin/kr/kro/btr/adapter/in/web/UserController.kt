package kr.kro.btr.adapter.`in`.web

import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import kr.kro.btr.adapter.`in`.web.payload.ModifyUserRequest
import kr.kro.btr.adapter.`in`.web.payload.ModifyUserResponse
import kr.kro.btr.adapter.`in`.web.payload.SignUpRequest
import kr.kro.btr.adapter.`in`.web.payload.SignUpResponse
import kr.kro.btr.adapter.`in`.web.payload.UserDetailResponse
import kr.kro.btr.adapter.`in`.web.proxy.UserProxy
import kr.kro.btr.adapter.out.persistence.UserRefreshTokenRepository
import kr.kro.btr.config.properties.AppProperties
import kr.kro.btr.core.converter.UserConverter
import kr.kro.btr.support.TokenDetail
import kr.kro.btr.support.annotation.AuthUser
import kr.kro.btr.support.oauth.token.AuthTokenProvider
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val userConverter: UserConverter,
    private val userProxy: UserProxy,
    // TODO
    private val authTokenProvider: AuthTokenProvider,
    private val userRefreshTokenRepository: UserRefreshTokenRepository,
    private val appProperties: AppProperties
) {

    @PostMapping("/refresh", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun signIn(request: HttpServletRequest): ResponseEntity<Void> {
        // TODO
        return ResponseEntity.noContent().build()
    }

    @PutMapping("/sign-up", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun signUp(@AuthUser my: TokenDetail, @RequestBody @Valid request: SignUpRequest): ResponseEntity<SignUpResponse> {
        val createdUserName = userProxy.signUp(my, request)
        return ResponseEntity.ok(SignUpResponse(createdUserName))
    }

    @DeleteMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun removeAccount(@AuthUser my: TokenDetail) {
        userProxy.remove(my.id)
    }

    @GetMapping("/my", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun detail(@AuthUser my: TokenDetail): ResponseEntity<UserDetailResponse> {
        val user = userProxy.search(my)
        return ResponseEntity.ok(userConverter.map(user))
    }

    @GetMapping("/{userId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun detail(@PathVariable userId: Long): ResponseEntity<UserDetailResponse> {
        val user = userProxy.search(userId)
        val userDetailResponse = userConverter.map(user)
        return ResponseEntity.ok(userDetailResponse)
    }

    @PutMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun modify(@AuthUser my: TokenDetail, @RequestBody @Valid request: ModifyUserRequest): ResponseEntity<ModifyUserResponse> {
        val modifiedUser = userProxy.modify(my, request)
        val modifyUserResponse = userConverter.mapToModifyUserResponse(modifiedUser)
        return ResponseEntity.ok(modifyUserResponse)
    }
}
