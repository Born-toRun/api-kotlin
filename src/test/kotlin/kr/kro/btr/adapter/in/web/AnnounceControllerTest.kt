package kr.kro.btr.adapter.`in`.web

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import kr.kro.btr.adapter.`in`.web.payload.*
import kr.kro.btr.adapter.`in`.web.proxy.AnnounceProxy
import kr.kro.btr.common.base.ControllerDescribeSpec
import kr.kro.btr.domain.port.model.result.AnnounceResult
import kr.kro.btr.utils.andExpectData
import kr.kro.btr.utils.restdocs.*
import kr.kro.btr.utils.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.restdocs.ManualRestDocumentation
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.request
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.web.context.WebApplicationContext
import java.time.LocalDateTime

@WebMvcTest(AnnounceController::class)
class AnnounceControllerTest (
    @MockkBean
    private val proxy: AnnounceProxy,
    @Autowired
    private val context: WebApplicationContext
): ControllerDescribeSpec ({

    val baseUrl = "/api/v1/announces"
    val restDocumentation = ManualRestDocumentation()
    val mockMvc = restDocMockMvcBuild(context, restDocumentation)

    beforeEach { restDocumentation.beforeTest(javaClass, it.name.testName) }
    afterEach { restDocumentation.afterTest() }

    describe("POST : $baseUrl") {
        val url = baseUrl
        val requestBody = CreateAnnounceRequest(
            title = "title",
            contents = "contents",
            postedAt = LocalDateTime.now()
        )

        context("공지를 등록 하면") {
            val requestJson = toJson(requestBody)
            val request = request(HttpMethod.POST, url)
                .contentType(APPLICATION_JSON)
                .content(requestJson)

            it("201 Created") {
                every { proxy.create(any(), any()) } just runs

                mockMvc.perform(request)
                    .andExpect(status().isCreated)
                    .andDocument(
                        "create-announces",
                        requestBody(
                            "title" type STRING means "제목" isRequired true,
                            "contents" type STRING means "내용" isRequired true,
                            "postedAt" type DATETIME means "게시일자" isRequired true,
                        )
                    )
            }
        }
    }

    describe("PUT : $baseUrl/{announceId}") {
        val url = "$baseUrl/{announceId}"
        val announceId = 0L
        val requestBody = ModifyAnnounceRequest(
            title = "title",
            contents = "contents",
            postedAt = LocalDateTime.now()
        )

        val announceResult = AnnounceResult(
            id = 0L,
            title = requestBody.title,
            contents = requestBody.contents,
            writer = AnnounceResult.Writer(
                userId = 0L,
                name = "테스트 계정"
            )
        )

        val response = ModifyAnnounceResponse(
            id = announceResult.id,
            title = announceResult.title,
            contents = announceResult.contents
        )

        context("공지를 수정 하면") {
            val requestJson = toJson(requestBody)
            val request = request(HttpMethod.PUT, url, announceId)
                .contentType(APPLICATION_JSON)
                .content(requestJson)

            it("200 OK") {
                every { proxy.modify(any(), any()) } returns announceResult

                mockMvc.perform(request)
                    .andExpect(status().isOk)
                    .andExpectData(
                        jsonPath("$.id") shouldBe response.id,
                        jsonPath("$.title") shouldBe response.title,
                        jsonPath("$.contents") shouldBe response.contents
                    )
                    .andDocument(
                        "modify-announces",
                        pathParameters(
                            "announceId" isRequired true pathMeans "식별자"
                        ),
                        requestBody(
                            "title" type STRING means "제목" isRequired true,
                            "contents" type STRING means "내용" isRequired true,
                            "postedAt" type DATETIME means "게시일자" isRequired true
                        ),
                        responseBody(
                            "id" type NUMBER means "식별자" isRequired true,
                            "title" type STRING means "제목" isRequired true,
                            "contents" type STRING means "내용" isRequired true,
                        )
                    )
            }
        }
    }

    describe("DELETE : $baseUrl/{announceId}") {
        val url = "$baseUrl/{announceId}"
        val announceId = 0L

        context("삭제를 하면") {
            val request = request(HttpMethod.DELETE, url, announceId)
                .contentType(APPLICATION_JSON)

            it("200 OK") {
                every { proxy.remove(any()) } just runs

                mockMvc.perform(request)
                    .andExpect(status().isOk)
                    .andDocument(
                        "remove-announces",
                        pathParameters(
                            "announceId" isRequired true pathMeans "식별자"
                        )
                    )
            }
        }
    }

    describe("GET : $baseUrl") {
        val url = baseUrl
        val announceResult = AnnounceResult(
            id = 0L,
            title = "title",
            contents = "contents",
            writer = AnnounceResult.Writer(
                userId = 0L,
                name = "테스트 계정"
            )
        )
        val announceResults = listOf(announceResult)
        val response = SearchAnnouncesResponse(listOf(SearchAnnouncesResponse.Detail(
            id = announceResult.id,
            title = announceResult.title,
            contents = announceResult.contents,
            writer = SearchAnnouncesResponse.Writer(
                userId = announceResult.writer.userId,
                name = announceResult.writer.name
            )
        )))

        context("공지 목록을 조회 하면") {
            val request = request(HttpMethod.GET, url)
                .contentType(APPLICATION_JSON)

            it("200 OK") {
                every { proxy.searchAll() } returns announceResults

                mockMvc.perform(request)
                    .andExpect(status().isOk)
                    .andExpectData(
                        jsonPath("$.details[0].id") shouldBe response.details[0].id,
                        jsonPath("$.details[0].title") shouldBe response.details[0].title,
                        jsonPath("$.details[0].contents") shouldBe response.details[0].contents,
                        jsonPath("$.details[0].writer.userId") shouldBe response.details[0].writer.userId,
                        jsonPath("$.details[0].writer.name") shouldBe response.details[0].writer.name
                    )
                    .andDocument(
                        "search-announces",
                        responseBody(
                            "details" type ARRAY means "등록된 공지 목록" isRequired true
                        )
                            .andWithPrefix("details[].", getAnnounceDetailsResponseSnippet())
                    )
            }
        }
    }

    describe("GET : $baseUrl") {
        val url = "$baseUrl/{announceId}"
        val announceId = 0L
        val announceResult = AnnounceResult(
            id = announceId,
            title = "title",
            contents = "contents",
            writer = AnnounceResult.Writer(
                userId = 0L,
                name = "테스트 계정"
            )
        )
        val response = DetailAnnounceResponse(
            id = announceResult.id,
            title = announceResult.title,
            contents = announceResult.contents,
            writer = DetailAnnounceResponse.Writer(
                userId = announceResult.writer.userId,
                name = announceResult.writer.name
            )
        )

        context("공지를 조회 하면") {
            val request = request(HttpMethod.GET, url, announceId)
                .contentType(APPLICATION_JSON)

            it("200 OK") {
                every { proxy.detail( any() ) } returns announceResult

                mockMvc.perform(request)
                    .andExpect(status().isOk)
                    .andExpectData(
                        jsonPath("$.id") shouldBe response.id,
                        jsonPath("$.title") shouldBe response.title,
                        jsonPath("$.contents") shouldBe response.contents,
                        jsonPath("$.writer.userId") shouldBe response.writer.userId,
                        jsonPath("$.writer.name") shouldBe response.writer.name
                    )
                    .andDocument(
                        "detail-announces",
                        pathParameters(
                            "announceId" isRequired true pathMeans "식별자"
                        ),
                        responseBody(
                            "id" type NUMBER means "식별자" isRequired true,
                            "title" type STRING means "제목" isRequired true,
                            "contents" type STRING means "내용" isRequired true,
                            "writer" type OBJECT means "작성자" isRequired true,
                            "writer.userId" type NUMBER means "작성자 식별자" isRequired false,
                            "writer.name" type STRING means "작성자 명" isRequired false
                        )
                    )
            }
        }
    }
}) {
    companion object {
        fun getAnnounceDetailsResponseSnippet(): List<FieldDescriptor> {
            return descriptor(
                "id" type NUMBER means "식별자" isRequired true,
                "title" type STRING means "제목" isRequired true,
                "contents" type STRING means "내용" isRequired true,
                "writer" type OBJECT means "작성자" isRequired true,
                "writer.userId" type NUMBER means "작성자 식별자" isRequired false,
                "writer.name" type STRING means "작성자 명" isRequired false
            )
        }

        private fun descriptor(vararg fields: RestDocsField): List<FieldDescriptor> {
            return fields.map { it.descriptor }
        }
    }
}
