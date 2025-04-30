package kr.kro.btr.adapter.`in`.web

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import kr.kro.btr.adapter.`in`.web.payload.BookmarkMarathonResponse
import kr.kro.btr.adapter.`in`.web.payload.SearchAllMarathonResponse
import kr.kro.btr.adapter.`in`.web.payload.SearchMarathonDetailResponse
import kr.kro.btr.adapter.`in`.web.proxy.MarathonProxy
import kr.kro.btr.common.base.ControllerDescribeSpec
import kr.kro.btr.core.converter.MarathonConverter
import kr.kro.btr.domain.port.model.Marathon
import kr.kro.btr.domain.port.model.MarathonDetail
import kr.kro.btr.utils.andExpectData
import kr.kro.btr.utils.restdocs.ARRAY
import kr.kro.btr.utils.restdocs.BOOLEAN
import kr.kro.btr.utils.restdocs.NUMBER
import kr.kro.btr.utils.restdocs.RestDocsField
import kr.kro.btr.utils.restdocs.STRING
import kr.kro.btr.utils.restdocs.andDocument
import kr.kro.btr.utils.restdocs.restDocMockMvcBuild
import kr.kro.btr.utils.restdocs.type
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
import java.time.format.DateTimeFormatter

@WebMvcTest(MarathonController::class)
class MarathonControllerTest (
    @MockkBean
    private val converter: MarathonConverter,
    @MockkBean
    private val proxy: MarathonProxy,
    @Autowired
    private val context: WebApplicationContext
): ControllerDescribeSpec ({

    val baseUrl = "/api/v1/marathons"
    val restDocumentation = ManualRestDocumentation()
    val mockMvc = restDocMockMvcBuild(context, restDocumentation)

    beforeEach { restDocumentation.beforeTest(javaClass, it.name.testName) }
    afterEach { restDocumentation.afterTest() }

    describe("GET : $baseUrl") {
        val url = baseUrl
        val marathons = listOf(Marathon(
            id = 0,
            title = "title",
            schedule = "schedule",
            venue = "venue",
            course = "course",
            isBookmarking = true
        ))
        val response = SearchAllMarathonResponse(
            marathons = listOf(
                SearchAllMarathonResponse.Marathon(
                    id = marathons[0].id,
                    title = marathons[0].title,
                    schedule = marathons[0].schedule,
                    venue = marathons[0].venue,
                    course = marathons[0].course,
                    isBookmarking = marathons[0].isBookmarking
                )
            )
        )
        context("조회를 하면") {
            val request = request(HttpMethod.GET, url)
                .contentType(APPLICATION_JSON)

            it("200 OK") {
                every { proxy.search(any(), any()) } returns marathons
                every { converter.map(any<List<Marathon>>()) } returns response

                mockMvc.perform(request)
                    .andExpect(status().isOk)
                    .andExpectData(
                        jsonPath("$.marathons[0].id") shouldBe response.marathons[0].id,
                        jsonPath("$.marathons[0].title") shouldBe response.marathons[0].title,
                        jsonPath("$.marathons[0].schedule") shouldBe response.marathons[0].schedule,
                        jsonPath("$.marathons[0].venue") shouldBe response.marathons[0].venue,
                        jsonPath("$.marathons[0].course") shouldBe response.marathons[0].course,
                        jsonPath("$.marathons[0].bookmarking") shouldBe response.marathons[0].isBookmarking,
                    )
                    .andDocument(
                        "search-marathons",
                        envelopeResponseBody(
                            "marathons" type ARRAY means "대회 목록"
                        )
                            .andWithPrefix("marathons[].", getMarathonsResponseSnippet())
                    )
            }
        }
    }

    describe("GET : $baseUrl/{marathonId}") {
        val marathonId = 0
        val url = "$baseUrl/$marathonId"
        val marathonDetail = MarathonDetail(
            id = 0,
            title = "title",
            owner = "owner",
            email = "email",
            schedule = "schedule",
            contact = "contact",
            course = "course",
            location = "location",
            venue = "venue",
            host = "host",
            duration = "duration",
            homepage = "homepage",
            venueDetail = "venueDetail",
            remark = "remark",
            registeredAt = LocalDateTime.now(),
            isBookmarking = true
        )
        val response = SearchMarathonDetailResponse(
            id = marathonDetail.id,
            title = marathonDetail.title,
            owner = marathonDetail.owner,
            email = marathonDetail.email,
            schedule = marathonDetail.schedule,
            contact = marathonDetail.contact,
            course = marathonDetail.course,
            location = marathonDetail.location,
            venue = marathonDetail.venue,
            host = marathonDetail.host,
            duration = marathonDetail.duration,
            homepage = marathonDetail.homepage,
            venueDetail = marathonDetail.venueDetail,
            remark = marathonDetail.remark,
            registeredAt = LocalDateTime.parse(marathonDetail.registeredAt.format(FORMATTER), FORMATTER),
            isBookmarking = marathonDetail.isBookmarking
        )

        context("조회를 하면") {
            val request = request(HttpMethod.GET, url)
                .contentType(APPLICATION_JSON)

            it("200 OK") {
                every { proxy.detail(any(), any()) } returns marathonDetail
                every { converter.map(any<MarathonDetail>()) } returns response

                mockMvc.perform(request)
                    .andExpect(status().isOk)
                    .andExpectData(
                        jsonPath("$.id") shouldBe response.id,
                        jsonPath("$.title") shouldBe response.title,
                        jsonPath("$.owner") shouldBe response.owner,
                        jsonPath("$.email") shouldBe response.email,
                        jsonPath("$.schedule") shouldBe response.schedule,
                        jsonPath("$.contact") shouldBe response.contact,
                        jsonPath("$.course") shouldBe response.course,
                        jsonPath("$.location") shouldBe response.location,
                        jsonPath("$.venue") shouldBe response.venue,
                        jsonPath("$.host") shouldBe response.host,
                        jsonPath("$.duration") shouldBe response.duration,
                        jsonPath("$.homepage") shouldBe response.homepage,
                        jsonPath("$.venueDetail") shouldBe response.venueDetail,
                        jsonPath("$.remark") shouldBe response.remark,
                        jsonPath("$.registeredAt") shouldBe response.registeredAt,
                        jsonPath("$.bookmarking") shouldBe response.isBookmarking,
                    )
                    .andDocument(
                        "search-marathon-detail",
                        envelopeResponseBody(
                            "id" type NUMBER means "식별자",
                            "title" type STRING means "대회명",
                            "owner" type STRING means "대표자명",
                            "email" type STRING means "대표 이메일",
                            "schedule" type STRING means "대회일시",
                            "contact" type STRING means "전화번호",
                            "course" type STRING means "대회종목",
                            "location" type STRING means "대회지역",
                            "venue" type STRING means "대회장소",
                            "host" type STRING means "주최단체",
                            "duration" type STRING means "접수기간",
                            "homepage" type STRING means "홈페이지",
                            "venueDetail" type STRING means "대회장",
                            "remark" type STRING means "기타소개",
                            "registeredAt" type STRING means "등록 일시",
                            "bookmarking" type BOOLEAN means "북마크 여부"
                        )
                    )
            }
        }
    }

    describe("POST : $baseUrl/bookmark/{marathonId}") {
        val marathonId = 0L
        val url = "$baseUrl/bookmark/$marathonId"
        val response = BookmarkMarathonResponse(
            marathonId = marathonId
        )

        context("조회를 하면") {
            val request = request(HttpMethod.POST, url)
                .contentType(APPLICATION_JSON)

            it("200 OK") {
                every { proxy.bookmark(any(), any()) } returns marathonId

                mockMvc.perform(request)
                    .andExpect(status().isOk)
                    .andExpectData(
                        jsonPath("$.marathonId") shouldBe response.marathonId
                    )
                    .andDocument(
                        "bookmark-marathon",
                        envelopeResponseBody(
                            "marathonId" type NUMBER means "북마크한 마라톤 식별자"
                        )
                    )
            }
        }
    }

    describe("DELETE : $baseUrl/bookmark/{marathonId}") {
        val marathonId = 0L
        val url = "$baseUrl/bookmark/$marathonId"
        val response = BookmarkMarathonResponse(
            marathonId = marathonId
        )

        context("조회를 하면") {
            val request = request(HttpMethod.DELETE, url)
                .contentType(APPLICATION_JSON)

            it("200 OK") {
                every { proxy.cancelBookmark(any(), any()) } returns marathonId

                mockMvc.perform(request)
                    .andExpect(status().isOk)
                    .andExpectData(
                        jsonPath("$.marathonId") shouldBe response.marathonId
                    )
                    .andDocument(
                        "cancel-bookmark-marathon",
                        envelopeResponseBody(
                            "marathonId" type NUMBER means "북마크 취소한 마라톤 식별자"
                        )
                    )
            }
        }
    }
}) {
    companion object {
        val FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")

        fun getMarathonsResponseSnippet(): List<FieldDescriptor> {
            return responseMarathonsResponse(
                "id" type NUMBER means "식별자",
                "title" type STRING means "대회명",
                "schedule" type STRING means "대회일시",
                "venue" type STRING means "대회장소",
                "course" type STRING means "대회종목",
                "bookmarking" type BOOLEAN means "북마크 여부",
            )
        }

        private fun responseMarathonsResponse(vararg fields: RestDocsField): List<FieldDescriptor> {
            return fields.map { it.descriptor }
        }
    }
}
