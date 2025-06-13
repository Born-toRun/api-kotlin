package kr.kro.btr.adapter.`in`.web

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import kr.kro.btr.adapter.`in`.web.payload.DetailUserPrivacyResponse
import kr.kro.btr.adapter.`in`.web.payload.SettingUserPrivacyRequest
import kr.kro.btr.adapter.`in`.web.proxy.PrivacyProxy
import kr.kro.btr.common.base.ControllerDescribeSpec
import kr.kro.btr.domain.port.model.result.UserPrivacyResult
import kr.kro.btr.utils.andExpectData
import kr.kro.btr.utils.restdocs.BOOLEAN
import kr.kro.btr.utils.restdocs.andDocument
import kr.kro.btr.utils.restdocs.requestBody
import kr.kro.btr.utils.restdocs.responseBody
import kr.kro.btr.utils.restdocs.restDocMockMvcBuild
import kr.kro.btr.utils.restdocs.type
import kr.kro.btr.utils.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.restdocs.ManualRestDocumentation
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.request
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.web.context.WebApplicationContext

@WebMvcTest(PrivacyController::class)
class PrivacyControllerTest (
    @MockkBean
    private val proxy: PrivacyProxy,
    @Autowired
    private val context: WebApplicationContext
): ControllerDescribeSpec ({

    val baseUrl = "/api/v1/privacy"
    val restDocumentation = ManualRestDocumentation()
    val mockMvc = restDocMockMvcBuild(context, restDocumentation)

    beforeEach { restDocumentation.beforeTest(javaClass, it.name.testName) }
    afterEach { restDocumentation.afterTest() }

    describe("PUT : $baseUrl/users") {
        val url = "$baseUrl/users"
        val requestBody = SettingUserPrivacyRequest(
            isInstagramIdPublic = true
        )

        context("유저별 프라이버시를 수정 하면") {
            val requestJson = toJson(requestBody)
            val request = request(HttpMethod.PUT, url)
                .contentType(APPLICATION_JSON)
                .content(requestJson)

            it("200 OK") {
                every { proxy.modifyUserPrivacy(any(), any()) } just runs

                mockMvc.perform(request)
                    .andExpect(status().isOk)
                    .andDocument(
                        "modify-user-privacy",
                        requestBody(
                            "isInstagramIdPublic" type BOOLEAN means "인스타그램 공개 여부" isRequired true,
                        )
                    )
            }
        }
    }

    describe("GET : $baseUrl/users") {
        val url = "$baseUrl/users"
        val userPrivacyResult = UserPrivacyResult(
            id = 0,
            userId = 0,
            isInstagramIdPublic = true
        )
        val response = DetailUserPrivacyResponse(
            isInstagramIdPublic = userPrivacyResult.isInstagramIdPublic
        )

        context("유저별 프라이버시를 조회 하면") {
            val request = request(HttpMethod.GET, url)
                .contentType(APPLICATION_JSON)

            it("200 OK") {
                every { proxy.searchUserPrivacy(any()) } returns userPrivacyResult

                mockMvc.perform(request)
                    .andExpect(status().isOk)
                    .andExpectData(
                        jsonPath("$.isInstagramIdPublic") shouldBe response.isInstagramIdPublic
                    )
                    .andDocument(
                        "search-user-privacy",
                        responseBody(
                            "isInstagramIdPublic" type BOOLEAN means "인스타그램 공개 여부" isRequired true
                        )
                    )
            }
        }
    }
})
