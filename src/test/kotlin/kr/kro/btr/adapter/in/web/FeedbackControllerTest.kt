package kr.kro.btr.adapter.`in`.web

import com.ninjasquad.springmockk.MockkBean
import io.mockk.just
import io.mockk.runs
import io.mockk.every
import kr.kro.btr.adapter.`in`.web.payload.CreateFeedbackRequest
import kr.kro.btr.adapter.`in`.web.proxy.FeedbackProxy
import kr.kro.btr.common.base.ControllerDescribeSpec
import kr.kro.btr.domain.constant.FeedbackType
import kr.kro.btr.utils.restdocs.STRING
import kr.kro.btr.utils.restdocs.andDocument
import kr.kro.btr.utils.restdocs.isRequired
import kr.kro.btr.utils.restdocs.requestBody
import kr.kro.btr.utils.restdocs.restDocMockMvcBuild
import kr.kro.btr.utils.restdocs.type
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.restdocs.ManualRestDocumentation
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.request
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.web.context.WebApplicationContext

@WebMvcTest(FeedbackController::class)
class FeedbackControllerTest(
    @MockkBean
    private val proxy: FeedbackProxy,
    @Autowired
    private val context: WebApplicationContext
) : ControllerDescribeSpec({

    val baseUrl = "/api/v1/feedbacks"
    val restDocumentation = ManualRestDocumentation()
    val mockMvc = restDocMockMvcBuild(context, restDocumentation)

    beforeEach { restDocumentation.beforeTest(javaClass, it.name.testName) }
    afterEach { restDocumentation.afterTest() }

    describe("POST : $baseUrl") {
        val url = baseUrl

        context("문의 피드백을 등록하면") {
            val requestBody = CreateFeedbackRequest(
                feedbackType = FeedbackType.INQUIRY,
                content = "로그인이 안돼요. 도와주세요!"
            )

            every { proxy.create(any(), any()) } just runs

            val requestJson = toJson(requestBody)
            val request = request(HttpMethod.POST, url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)

            it("201 Created") {
                mockMvc.perform(request)
                    .andExpect(status().isCreated)
                    .andDocument(
                        "create-feedbacks",
                        requestBody(
                            "feedbackType" type STRING means "피드백 타입 (INQUIRY: 문의, BUG: 버그, IDEA: 아이디어)" isRequired true,
                            "content" type STRING means "피드백 내용" isRequired true
                        )
                    )
            }
        }

        context("버그 피드백을 등록하면") {
            val requestBody = CreateFeedbackRequest(
                feedbackType = FeedbackType.BUG,
                content = "앱이 자꾸 종료됩니다. 확인 부탁드립니다."
            )

            every { proxy.create(any(), any()) } just runs

            val requestJson = toJson(requestBody)
            val request = request(HttpMethod.POST, url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)

            it("201 Created") {
                mockMvc.perform(request)
                    .andExpect(status().isCreated)
            }
        }

        context("아이디어 피드백을 등록하면") {
            val requestBody = CreateFeedbackRequest(
                feedbackType = FeedbackType.IDEA,
                content = "러닝 기록 통계 기능을 추가해주세요!"
            )

            every { proxy.create(any(), any()) } just runs

            val requestJson = toJson(requestBody)
            val request = request(HttpMethod.POST, url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)

            it("201 Created") {
                mockMvc.perform(request)
                    .andExpect(status().isCreated)
            }
        }
    }
})
