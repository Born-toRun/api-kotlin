package kr.kro.btr.adapter.`in`.web

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import kr.kro.btr.adapter.`in`.web.payload.ModifyUserRequest
import kr.kro.btr.adapter.`in`.web.payload.ModifyUserResponse
import kr.kro.btr.adapter.`in`.web.payload.SignUpRequest
import kr.kro.btr.adapter.`in`.web.payload.UserDetailResponse
import kr.kro.btr.adapter.`in`.web.proxy.UserProxy
import kr.kro.btr.common.base.ControllerDescribeSpec
import kr.kro.btr.core.converter.UserConverter
import kr.kro.btr.domain.constant.ProviderType
import kr.kro.btr.domain.constant.RoleType
import kr.kro.btr.domain.port.model.BornToRunUser
import kr.kro.btr.support.TokenDetail
import kr.kro.btr.utils.andExpectData
import kr.kro.btr.utils.restdocs.BOOLEAN
import kr.kro.btr.utils.restdocs.NUMBER
import kr.kro.btr.utils.restdocs.STRING
import kr.kro.btr.utils.restdocs.andDocument
import kr.kro.btr.utils.restdocs.pathMeans
import kr.kro.btr.utils.restdocs.pathParameters
import kr.kro.btr.utils.restdocs.requestBody
import kr.kro.btr.utils.restdocs.responseBody
import kr.kro.btr.utils.restdocs.restDocMockMvcBuild
import kr.kro.btr.utils.restdocs.type
import kr.kro.btr.utils.shouldBe
import org.mockito.ArgumentMatchers.anyLong
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.restdocs.ManualRestDocumentation
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.request
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.web.context.WebApplicationContext
import java.time.LocalDateTime

@WebMvcTest(UserController::class)
class UserControllerTest (
    @MockkBean
    private val converter: UserConverter,
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

    //TODO: refresh
//    describe("POST : $baseUrl/refresh") {

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
                            "userName" type STRING means "성명" isOptional false,
                            "crewId" type NUMBER means "소속 크루 식별자" isOptional false,
                            "instagramId" type STRING means "인스타그램 아이디"  isOptional true
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
            refreshToken = "refreshToken",
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
        val response = UserDetailResponse(
            userId = user.userId,
            userName = user.userName,
            crewName = user.crewName,
            profileImageUri = user.profileImageUri,
            isAdmin = user.isAdmin,
            isManager = user.isManager,
            yellowCardQty = user.yellowCardQty,
            isInstagramIdPublic = user.isInstagramIdPublic,
            instagramId = user.instagramId
        )

        context("내 정보를 조회 하면") {
            val request = request(HttpMethod.GET, url)
                .contentType(APPLICATION_JSON)

            it("200 OK") {
                every { proxy.search(any<TokenDetail>()) } returns user
                every { converter.map(any<BornToRunUser>()) } returns response

                mockMvc.perform(request)
                    .andExpect(status().isOk)
                    .andDocument(
                        "my-detail",
                        responseBody(
                            "userId" type NUMBER means "식별자" isOptional false,
                            "userName" type STRING means "성명" isOptional true,
                            "crewName" type STRING means "소속 크루명" isOptional true,
                            "profileImageUri" type STRING means "프로필 이미지 uri" isOptional true,
                            "isAdmin" type BOOLEAN means "관리자 여부" isOptional false,
                            "isManager" type BOOLEAN means "크루장 여부" isOptional false,
                            "yellowCardQty" type NUMBER means "신고 개수" isOptional false,
                            "isInstagramIdPublic" type BOOLEAN means "인스타그램 공개 여부" isOptional true,
                            "instagramUri" type STRING means "인스타그램 uri" isOptional true
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
            refreshToken = "refreshToken",
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
        val response = UserDetailResponse(
            userId = user.userId,
            userName = user.userName,
            crewName = user.crewName,
            profileImageUri = user.profileImageUri,
            isAdmin = user.isAdmin,
            isManager = user.isManager,
            yellowCardQty = user.yellowCardQty,
            isInstagramIdPublic = user.isInstagramIdPublic,
            instagramId = user.instagramId
        )

        context("유저 정보를 조회 하면") {
            val request = request(HttpMethod.GET, url, userId)
                .contentType(APPLICATION_JSON)

            it("200 OK") {
                every { proxy.search(anyLong()) } returns user
                every { converter.map(any<BornToRunUser>()) } returns response

                mockMvc.perform(request)
                    .andExpect(status().isOk)
                    .andDocument(
                        "user-detail",
                        pathParameters(
                            "userId" pathMeans "대상 유저 식별자"
                        ),
                        responseBody(
                            "userId" type NUMBER means "식별자" isOptional false,
                            "userName" type STRING means "성명" isOptional true,
                            "crewName" type STRING means "소속 크루명" isOptional true,
                            "profileImageUri" type STRING means "프로필 이미지 uri" isOptional true,
                            "isAdmin" type BOOLEAN means "관리자 여부" isOptional false,
                            "isManager" type BOOLEAN means "크루장 여부" isOptional false,
                            "yellowCardQty" type NUMBER means "신고 개수" isOptional false,
                            "isInstagramIdPublic" type BOOLEAN means "인스타그램 공개 여부" isOptional true,
                            "instagramUri" type STRING means "인스타그램 uri" isOptional true
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
            refreshToken = "refreshToken",
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
        val response = ModifyUserResponse(
            userName = "userName",
            crewName = "crewName",
            instagramId = requestBody.instagramId,
            profileImageUri = "profileImageUri"
        )

        context("정보를 수정 하면") {
            val requestJson = toJson(requestBody)
            val request = request(HttpMethod.PUT, url)
                .contentType(APPLICATION_JSON)
                .content(requestJson)

            it("201 Created") {
                every { proxy.modify(any(), any<ModifyUserRequest>()) } returns user
                every { converter.mapToModifyUserResponse(any<BornToRunUser>()) } returns response

                mockMvc.perform(request)
                    .andExpect(status().isOk)
                    .andDocument(
                        "modify-users",
                        requestBody(
                            "profileImageId" type NUMBER means "프로필 이미지 식별자" isOptional true,
                            "instagramId" type STRING means "인스타그램 아이디" isOptional true
                        ),
                        responseBody(
                            "userName" type STRING means "유저명" isOptional true,
                            "crewName" type STRING means "소속 크루명" isOptional true,
                            "instagramId" type STRING means "인스타그램 아이디" isOptional true,
                            "profileImageUri" type STRING means "프로필 이미지 uri" isOptional true
                        )
                    )
            }
        }
    }
})
