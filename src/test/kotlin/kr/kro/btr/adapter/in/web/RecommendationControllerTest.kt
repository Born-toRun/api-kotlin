package kr.kro.btr.adapter.`in`.web

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import kr.kro.btr.adapter.`in`.web.proxy.RecommendationProxy
import kr.kro.btr.common.base.ControllerDescribeSpec
import kr.kro.btr.domain.constant.RecommendationType
import kr.kro.btr.utils.restdocs.andDocument
import kr.kro.btr.utils.restdocs.pathMeans
import kr.kro.btr.utils.restdocs.pathParameters
import kr.kro.btr.utils.restdocs.restDocMockMvcBuild
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.restdocs.ManualRestDocumentation
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.request
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.web.context.WebApplicationContext

@WebMvcTest(RecommendationController::class)
class RecommendationControllerTest (
    @MockkBean
    private val proxy: RecommendationProxy,
    @Autowired
    private val context: WebApplicationContext
): ControllerDescribeSpec ({

    val baseUrl = "/api/v1/recommendations"
    val restDocumentation = ManualRestDocumentation()
    val mockMvc = restDocMockMvcBuild(context, restDocumentation)

    beforeEach { restDocumentation.beforeTest(javaClass, it.name.testName) }
    afterEach { restDocumentation.afterTest() }

    describe("POST : $baseUrl/{recommendationType}/{contentId}") {
        val type = RecommendationType.FEED
        val id = 0L
        val url = "$baseUrl/{recommendationType}/{contentId}"

        context("좋아요를 하면") {
            val request = request(HttpMethod.POST, url, type, id)
                .contentType(APPLICATION_JSON)

            it("201 Created") {
                every { proxy.create(any(), any(), any()) } just runs

                mockMvc.perform(request)
                    .andExpect(status().isCreated)
                    .andDocument(
                        "recommended",
                        pathParameters(
                            "recommendationType" pathMeans "좋아요 타입(FEED, COMMENT)",
                            "contentId" pathMeans "좋아요 컨텐츠 식별자"
                        )
                    )
            }
        }
    }

    describe("DELETE : $baseUrl/{recommendationType}/{contentId}") {
        val type = RecommendationType.FEED
        val id = 0L
        val url = "$baseUrl/{recommendationType}/{contentId}"

        context("좋아요 취소를 하면") {
            val request = request(HttpMethod.DELETE, url, type, id)
                .contentType(APPLICATION_JSON)

            it("200 OK") {
                every { proxy.remove(any(), any(), any()) } just runs

                mockMvc.perform(request)
                    .andExpect(status().isOk)
                    .andDocument(
                        "cancel-recommendation",
                        pathParameters(
                            "recommendationType" pathMeans "좋아요 타입(FEED, COMMENT)",
                            "contentId" pathMeans "좋아요 컨텐츠 식별자"
                        )
                    )
            }
        }
    }
})
