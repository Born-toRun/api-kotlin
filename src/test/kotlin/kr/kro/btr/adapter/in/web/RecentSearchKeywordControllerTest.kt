package kr.kro.btr.adapter.`in`.web

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import kr.kro.btr.adapter.`in`.web.proxy.RecentSearchKeywordProxy
import kr.kro.btr.common.base.ControllerDescribeSpec
import kr.kro.btr.utils.restdocs.ARRAY
import kr.kro.btr.utils.restdocs.andDocument
import kr.kro.btr.utils.restdocs.pathMeans
import kr.kro.btr.utils.restdocs.pathParameters
import kr.kro.btr.utils.restdocs.responseBody
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

@WebMvcTest(RecentSearchKeywordController::class)
class RecentSearchKeywordControllerTest (
    @MockkBean
    private val proxy: RecentSearchKeywordProxy,
    @Autowired
    private val context: WebApplicationContext
): ControllerDescribeSpec ({

    val baseUrl = "/api/v1/recent-search-keywords"
    val restDocumentation = ManualRestDocumentation()
    val mockMvc = restDocMockMvcBuild(context, restDocumentation)

    beforeEach { restDocumentation.beforeTest(javaClass, it.name.testName) }
    afterEach { restDocumentation.afterTest() }

    describe("POST : $baseUrl/{keyword}") {
        val url = "$baseUrl/{keyword}"
        val keyword = "keyword"

        context("키워드로 검색 하면") {
            val request = request(HttpMethod.POST, url, keyword)
                .contentType(APPLICATION_JSON)

            it("201 Created") {
                every { proxy.add(any(), any()) } just runs

                mockMvc.perform(request)
                    .andExpect(status().isCreated)
                    .andDocument(
                        "create-recent-search-keywords",
                        pathParameters(
                            "keyword" pathMeans "검색 키워드"
                        )
                    )
            }
        }
    }

    describe("DELETE : $baseUrl") {
        val url = baseUrl

        context("일괄 삭제 하면") {
            val request = request(HttpMethod.DELETE, url)
                .contentType(APPLICATION_JSON)

            it("200 OK") {
                every { proxy.removeAll(any()) } just runs

                mockMvc.perform(request)
                    .andExpect(status().isOk)
                    .andDocument(
                        "remove-all-recent-search-keywords"
                    )
            }
        }
    }

    describe("DELETE : $baseUrl/{keyword}") {
        val url = "$baseUrl/{keyword}"
        val keyword = "keyword"

        context("검색어 삭제 하면") {
            val request = request(HttpMethod.DELETE, url, keyword)
                .contentType(APPLICATION_JSON)

            it("200 OK") {
                every { proxy.removeKeyword(any(), any()) } just runs

                mockMvc.perform(request)
                    .andExpect(status().isOk)
                    .andDocument(
                        "remove-recent-search-keywords",
                        pathParameters(
                            "keyword" pathMeans "삭제할 검색 키워드"
                        )
                    )
            }
        }
    }

    describe("GET : $baseUrl") {
        val url = baseUrl
        val recentSearchKeywords = listOf("keyword1", "keyword2")

        context("키워드로 검색 하면") {
            val request = request(HttpMethod.GET, url)
                .contentType(APPLICATION_JSON)

            it("200 OK") {
                every { proxy.search(any()) } returns recentSearchKeywords

                mockMvc.perform(request)
                    .andExpect(status().isOk)
                    .andDocument(
                        "search-recent-search-keywords",
                        responseBody(
                            "searchKeywords" type ARRAY means "최근 검색어 목록" isRequired true
                        )
                    )
            }
        }
    }
})
