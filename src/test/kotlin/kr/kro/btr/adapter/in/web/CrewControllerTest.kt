package kr.kro.btr.adapter.`in`.web

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import kr.kro.btr.adapter.`in`.web.payload.CreateCrewRequest
import kr.kro.btr.adapter.`in`.web.payload.DetailCrewResponse
import kr.kro.btr.adapter.`in`.web.payload.SearchCrewResponse
import kr.kro.btr.adapter.`in`.web.proxy.CrewProxy
import kr.kro.btr.common.base.ControllerDescribeSpec
import kr.kro.btr.core.converter.CrewConverter
import kr.kro.btr.domain.port.model.Crew
import kr.kro.btr.utils.andExpectData
import kr.kro.btr.utils.restdocs.ARRAY
import kr.kro.btr.utils.restdocs.NUMBER
import kr.kro.btr.utils.restdocs.RestDocsField
import kr.kro.btr.utils.restdocs.STRING
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
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.web.context.WebApplicationContext

@WebMvcTest(CrewController::class)
class CrewControllerTest (
    @MockkBean
    private val converter: CrewConverter,
    @MockkBean
    private val proxy: CrewProxy,
    @Autowired
    private val context: WebApplicationContext
): ControllerDescribeSpec ({

    val baseUrl = "/api/v1/crews"
    val restDocumentation = ManualRestDocumentation()
    val mockMvc = restDocMockMvcBuild(context, restDocumentation)

    beforeEach { restDocumentation.beforeTest(javaClass, it.name.testName) }
    afterEach { restDocumentation.afterTest() }

    describe("GET : $baseUrl") {
        val url = baseUrl
        val crew = Crew(
            id = 0,
            name = "crewName",
            contents = "contents",
            region = "region",
            imageUri = "imageUri",
            logoUri = "logoUri",
            sns = "crewSnsUri"
        )
        val crews = listOf(crew)
        val response = SearchCrewResponse(listOf(SearchCrewResponse.CrewDetail(
            id = crew.id,
            crewName = crew.name,
            contents = crew.contents,
            region = crew.region,
            imageUri = crew.imageUri,
            logoUri = crew.logoUri,
            crewSnsUri = crew.sns
        )))

        context("조회를 하면") {
            val request = request(HttpMethod.GET, url)
                .contentType(APPLICATION_JSON)

            it("200 OK") {
                every { proxy.searchAll() } returns crews
                every { converter.map(any<List<Crew>>()) } returns response

                mockMvc.perform(request)
                    .andExpect(status().isOk)
                    .andExpectData(
                        jsonPath("$.crewDetails[0].id") shouldBe response.crewDetails[0].id,
                        jsonPath("$.crewDetails[0].crewName") shouldBe response.crewDetails[0].crewName,
                        jsonPath("$.crewDetails[0].contents") shouldBe response.crewDetails[0].contents,
                        jsonPath("$.crewDetails[0].region") shouldBe response.crewDetails[0].region,
                        jsonPath("$.crewDetails[0].imageUri") shouldBe response.crewDetails[0].imageUri,
                        jsonPath("$.crewDetails[0].logoUri") shouldBe response.crewDetails[0].logoUri,
                        jsonPath("$.crewDetails[0].crewSnsUri") shouldBe response.crewDetails[0].crewSnsUri
                    )
                    .andDocument(
                        "search-crews",
                        responseBody(
                            "crewDetails" type ARRAY means "등록된 크루 목록" isOptional false
                        )
                            .andWithPrefix("crewDetails[].", getCrewDetailsResponseSnippet())
                    )
            }
        }
    }

    describe("GET : $baseUrl/{crewId}") {
        val url = "$baseUrl/{crewId}"
        val crewId = 0L
        val crew = Crew(
            id = 0,
            name = "crewName",
            contents = "contents",
            region = "region",
            imageUri = "imageUri",
            logoUri = "logoUri",
            sns = "crewSnsUri"
        )
        val response = DetailCrewResponse(
            id = crew.id,
            crewName = crew.name,
            contents = crew.contents,
            region = crew.region,
            imageUri = crew.imageUri,
            logoUri = crew.logoUri,
            crewSnsUri = crew.sns
        )

        context("조회를 하면") {
            val request = request(HttpMethod.GET, url, crewId)
                .contentType(APPLICATION_JSON)

            it("200 OK") {
                every { proxy.detail(any()) } returns crew
                every { converter.map(any<Crew>()) } returns response

                mockMvc.perform(request)
                    .andExpect(status().isOk)
                    .andExpectData(
                        jsonPath("$.id") shouldBe response.id,
                        jsonPath("$.crewName") shouldBe response.crewName,
                        jsonPath("$.contents") shouldBe response.contents,
                        jsonPath("$.region") shouldBe response.region,
                        jsonPath("$.imageUri") shouldBe response.imageUri,
                        jsonPath("$.logoUri") shouldBe response.logoUri,
                        jsonPath("$.crewSnsUri") shouldBe response.crewSnsUri
                    )
                    .andDocument(
                        "search-crew-detail",
                        responseBody(
                            "id" type NUMBER means "식별자" isOptional false,
                            "crewName" type STRING means "크루명" isOptional false,
                            "contents" type STRING means "크루 소개" isOptional false,
                            "region" type STRING means "크루 활동 지역" isOptional false,
                            "imageUri" type STRING means "크루 대표 이미지 uri" isOptional true,
                            "logoUri" type STRING means "크루 로고 uri" isOptional true,
                            "crewSnsUri" type STRING means "크루 sns uri" isOptional true
                        )
                    )
            }
        }
    }

    describe("POST : $baseUrl") {
        val url = baseUrl
        val requestBody = CreateCrewRequest(
            name = "name",
            contents = "contents",
            sns = "sns",
            region = "region"
        )

        context("등록을 하면") {
            every { proxy.create(any()) } just runs

            val requestJson = toJson(requestBody)
            val request = request(HttpMethod.POST, url)
                .contentType(APPLICATION_JSON)
                .content(requestJson)

            it("201 Created") {
                mockMvc.perform(request)
                    .andExpect(status().isCreated)
                    .andDocument(
                        "create-crews",
                        requestBody(
                            "name" type STRING means "크루명" isOptional false,
                            "contents" type STRING means "크루소개" isOptional false,
                            "sns" type STRING means "크루 sns uri" isOptional true,
                            "region" type STRING means "크루 활동 지역" isOptional false
                        )
                    )
            }
        }
    }
}) {
    companion object {
        fun getCrewDetailsResponseSnippet(): List<FieldDescriptor> {
            return descriptor(
                "id" type NUMBER means "식별자" isOptional false,
                "crewName" type STRING means "크루명" isOptional false,
                "contents" type STRING means "크루 소개" isOptional false,
                "region" type STRING means "크루 활동 지역" isOptional false,
                "imageUri" type STRING means "크루 대표 이미지 uri" isOptional true,
                "logoUri" type STRING means "크루 로고 uri" isOptional true,
                "crewSnsUri" type STRING means "크루 sns uri" isOptional true
            )
        }

        private fun descriptor(vararg fields: RestDocsField): List<FieldDescriptor> {
            return fields.map { it.descriptor }
        }
    }
}
