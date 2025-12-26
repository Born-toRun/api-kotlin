package kr.kro.btr.adapter.`in`.web

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import jakarta.servlet.http.Cookie
import kr.kro.btr.adapter.`in`.web.payload.ModifyUserRequest
import kr.kro.btr.adapter.`in`.web.payload.SignUpRequest
import kr.kro.btr.adapter.`in`.web.proxy.UserProxy
import kr.kro.btr.common.base.ControllerDescribeSpec
import kr.kro.btr.domain.constant.ProviderType
import kr.kro.btr.domain.constant.RoleType
import kr.kro.btr.domain.port.model.result.BornToRunUser
import kr.kro.btr.domain.port.model.result.RefreshTokenResult
import kr.kro.btr.support.TokenDetail
import kr.kro.btr.utils.andExpectData
import kr.kro.btr.utils.restdocs.*
import kr.kro.btr.utils.shouldBe
import org.mockito.ArgumentMatchers.anyLong
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.restdocs.ManualRestDocumentation
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.request
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.REFRESH_TOKEN
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.web.context.WebApplicationContext
import java.time.LocalDateTime

@WebMvcTest(UserController::class)
class UserControllerTest (
    @MockkBean
    private val proxy: UserProxy,
    @Autowired
    private val context: WebApplicationContext
): ControllerDescribeSpec ({

    val baseUrl = "/api/v1/users"
    val restDocumentation = ManualRestDocumentation()
    val mockMvc = restDocMockMvcBuild(context, restDocumentation)

    beforeEach { restDocumentation.beforeTest(javaClass, it.name.testName) }
    afterEach { restDocumentation.afterTest() }

    describe("POST : $baseUrl/refresh") {
        val url = "$baseUrl/refresh"
        val expiredAccessToken = "expired.access.token"
        val refreshTokenValue = "valid.refresh.token"
        val newAccessToken = "new.access.token"
        val newRefreshToken = "new.refresh.token"

        context("유효한 리프레시 토큰으로 액세스 토큰을 갱신하면") {
            val request = request(HttpMethod.POST, url)
                .header("Authorization", "Bearer $expiredAccessToken")
                .cookie(Cookie("refresh_token", refreshTokenValue))

            it("액세스 토큰과 새 리프레시 토큰을 반환한다") {
                every { proxy.refreshToken(any(), any()) } returns RefreshTokenResult(
                    accessToken = newAccessToken,
                    refreshToken = newRefreshToken,
                    cookieMaxAge = 604800 // 7 days in seconds
                )

                mockMvc.perform(request)
                    .andExpect(status().isOk)
                    .andExpectData(
                        jsonPath("$.accessToken") shouldBe newAccessToken,
                        jsonPath("$.refreshToken") shouldBe newRefreshToken
                    )
                    .andDocument(
                        "refresh-token",
                        requestHeaders(
                            HttpHeaders.AUTHORIZATION headerMeans "만료된 Bearer Access Token"
                        ),
                        requestCookies(
                            REFRESH_TOKEN cookieMeans "유효한 Refresh Token"
                        ),
                        responseBody(
                            "accessToken" type STRING means "인증 토큰" isRequired true,
                            "refreshToken" type STRING means "새로운 리프레시 토큰" isRequired true
                        )
                    )
            }
        }
    }

    describe("PUT : $baseUrl/sign-up") {
        val url = "$baseUrl/sign-up"
        val requestBody = SignUpRequest(
            userName = "userName",
            crewId = 0,
            instagramId = "instagramId"
        )

        context("회원가입 하면") {
            val requestJson = toJson(requestBody)
            val request = request(HttpMethod.PUT, url)
                .contentType(APPLICATION_JSON)
                .content(requestJson)

            it("200 OK") {
                every { proxy.signUp(any(), any()) } returns requestBody.userName

                mockMvc.perform(request)
                    .andExpect(status().isOk)
                    .andExpectData(
                        jsonPath("$.name") shouldBe requestBody.userName
                    )
                    .andDocument(
                        "sign-up",
                        requestBody(
                            "userName" type STRING means "성명" isRequired true,
                            "crewId" type NUMBER means "소속 크루 식별자" isRequired true,
                            "instagramId" type STRING means "인스타그램 아이디"  isRequired false
                        )
                    )
            }
        }
    }

    describe("DELETE : $baseUrl") {
        val url = baseUrl

        context("회원탈퇴 하면") {
            val request = request(HttpMethod.DELETE, url)
                .contentType(APPLICATION_JSON)

            it("200 OK") {
                every { proxy.remove(any()) } just runs

                mockMvc.perform(request)
                    .andExpect(status().isOk)
                    .andDocument(
                        "remove-users"
                    )
            }
        }
    }

    describe("GET : $baseUrl/my") {
        val url = "$baseUrl/my"
        val user = BornToRunUser(
            userId = 0,
            socialId = "socialId",
            providerType = ProviderType.KAKAO,
            roleType = RoleType.ADMIN,
            userName = "userName",
            crewId = 0,
            crewName = "crewName",
            instagramId = "instagramId",
            imageId = 0,
            profileImageUri = "profileImageUri",
            lastLoginAt = LocalDateTime.now(),
            isAdmin = true,
            isManager = false,
            yellowCardQty = 1,
            isInstagramIdPublic = true,
        )

        context("내 정보를 조회 하면") {
            val request = request(HttpMethod.GET, url)
                .contentType(APPLICATION_JSON)

            it("200 OK") {
                every { proxy.search(any<TokenDetail>()) } returns user

                mockMvc.perform(request)
                    .andExpect(status().isOk)
                    .andDocument(
                        "my-detail",
                        responseBody(
                            "userId" type NUMBER means "식별자" isRequired true,
                            "userName" type STRING means "성명" isRequired false,
                            "crewName" type STRING means "소속 크루명" isRequired false,
                            "profileImageUri" type STRING means "프로필 이미지 uri" isRequired false,
                            "isAdmin" type BOOLEAN means "관리자 여부" isRequired true,
                            "isManager" type BOOLEAN means "크루장 여부" isRequired true,
                            "yellowCardQty" type NUMBER means "신고 개수" isRequired true,
                            "isInstagramIdPublic" type BOOLEAN means "인스타그램 공개 여부" isRequired false,
                            "instagramId" type STRING means "인스타그램 아이디" isRequired false
                        )
                    )
            }
        }
    }

    describe("GET : $baseUrl/{userId}") {
        val url = "$baseUrl/{userId}"
        val userId = 0L
        val user = BornToRunUser(
            userId = userId,
            socialId = "socialId",
            providerType = ProviderType.KAKAO,
            roleType = RoleType.ADMIN,
            userName = "userName",
            crewId = 0,
            crewName = "crewName",
            instagramId = "instagramId",
            imageId = 0,
            profileImageUri = "profileImageUri",
            lastLoginAt = LocalDateTime.now(),
            isAdmin = true,
            isManager = false,
            yellowCardQty = 1,
            isInstagramIdPublic = true,
        )

        context("유저 정보를 조회 하면") {
            val request = request(HttpMethod.GET, url, userId)
                .contentType(APPLICATION_JSON)

            it("200 OK") {
                every { proxy.search(anyLong()) } returns user

                mockMvc.perform(request)
                    .andExpect(status().isOk)
                    .andDocument(
                        "user-detail",
                        pathParameters(
                            "userId" isRequired true pathMeans "대상 유저 식별자"
                        ),
                        responseBody(
                            "userId" type NUMBER means "식별자" isRequired true,
                            "userName" type STRING means "성명" isRequired false,
                            "crewName" type STRING means "소속 크루명" isRequired false,
                            "profileImageUri" type STRING means "프로필 이미지 uri" isRequired false,
                            "isAdmin" type BOOLEAN means "관리자 여부" isRequired true,
                            "isManager" type BOOLEAN means "크루장 여부" isRequired true,
                            "yellowCardQty" type NUMBER means "신고 개수" isRequired true,
                            "isInstagramIdPublic" type BOOLEAN means "인스타그램 공개 여부" isRequired false,
                            "instagramId" type STRING means "인스타그램 아이디" isRequired false
                        )
                    )
            }
        }
    }

    describe("PUT : $baseUrl") {
        val url = baseUrl
        val requestBody = ModifyUserRequest(
            profileImageId = 0,
            instagramId = "instagramId",
        )
        val user = BornToRunUser(
            userId = 0,
            socialId = "socialId",
            providerType = ProviderType.KAKAO,
            roleType = RoleType.ADMIN,
            userName = "userName",
            crewId = 0,
            crewName = "crewName",
            instagramId = "instagramId",
            imageId = 0,
            profileImageUri = "profileImageUri",
            lastLoginAt = LocalDateTime.now(),
            isAdmin = true,
            isManager = false,
            yellowCardQty = 1,
            isInstagramIdPublic = true,
        )

        context("정보를 수정 하면") {
            val requestJson = toJson(requestBody)
            val request = request(HttpMethod.PUT, url)
                .contentType(APPLICATION_JSON)
                .content(requestJson)

            it("201 Created") {
                every { proxy.modify(any(), any<ModifyUserRequest>()) } returns user

                mockMvc.perform(request)
                    .andExpect(status().isOk)
                    .andDocument(
                        "modify-users",
                        requestBody(
                            "profileImageId" type NUMBER means "프로필 이미지 식별자" isRequired false,
                            "instagramId" type STRING means "인스타그램 아이디" isRequired false
                        ),
                        responseBody(
                            "userName" type STRING means "유저명" isRequired false,
                            "crewName" type STRING means "소속 크루명" isRequired false,
                            "instagramId" type STRING means "인스타그램 아이디" isRequired false,
                            "profileImageUri" type STRING means "프로필 이미지 uri" isRequired false
                        )
                    )
            }
        }
    }
})
