package kr.kro.btr.adapter.`in`.web

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import kr.kro.btr.adapter.`in`.web.payload.CreateYellowCardRequest
import kr.kro.btr.adapter.`in`.web.proxy.YellowCardProxy
import kr.kro.btr.common.base.ControllerDescribeSpec
import kr.kro.btr.utils.restdocs.NUMBER
import kr.kro.btr.utils.restdocs.STRING
import kr.kro.btr.utils.restdocs.andDocument
import kr.kro.btr.utils.restdocs.requestBody
import kr.kro.btr.utils.restdocs.restDocMockMvcBuild
import kr.kro.btr.utils.restdocs.type
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.restdocs.ManualRestDocumentation
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.request
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.web.context.WebApplicationContext

@WebMvcTest(YellowCardController::class)
class YellowCardControllerTest (
    @MockkBean
    private val proxy: YellowCardProxy,
    @Autowired
    private val context: WebApplicationContext
): ControllerDescribeSpec ({

    val baseUrl = "/api/v1/yellow-cards"
    val restDocumentation = ManualRestDocumentation()
    val mockMvc = restDocMockMvcBuild(context, restDocumentation)

    beforeEach { restDocumentation.beforeTest(javaClass, it.name.testName) }
    afterEach { restDocumentation.afterTest() }

    describe("POST : $baseUrl") {
        val url = baseUrl
        val requestBody = CreateYellowCardRequest(
            targetUserId = 0,
            reason = "reason",
            basis = "basis",
        )

        context("신고를 하면") {
            every { proxy.create(any(), any()) } just runs

            val requestJson = toJson(requestBody)
            val request = request(HttpMethod.POST, url)
                .contentType(APPLICATION_JSON)
                .content(requestJson)

            it("201 Created") {
                mockMvc.perform(request)
                    .andExpect(status().isCreated)
                    .andDocument(
                        "create-yellow-cards",
                        requestBody(
                            "targetUserId" type NUMBER means "신고 대상 식별자" isOptional false,
                            "reason" type STRING means "신고사유" isOptional true,
                            "basis" type STRING means "신고 대상 게시글" isOptional false
                        )
                    )
            }
        }
    }
})
