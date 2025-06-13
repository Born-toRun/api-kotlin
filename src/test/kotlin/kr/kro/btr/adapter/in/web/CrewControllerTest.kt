package kr.kro.btr.adapter.`in`.web

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import kr.kro.btr.adapter.`in`.web.payload.CreateCrewRequest
import kr.kro.btr.adapter.`in`.web.payload.DetailCrewResponse
import kr.kro.btr.adapter.`in`.web.payload.SearchCrewsResponse
import kr.kro.btr.adapter.`in`.web.proxy.CrewProxy
import kr.kro.btr.common.base.ControllerDescribeSpec
import kr.kro.btr.domain.port.model.result.CrewResult
import kr.kro.btr.utils.andExpectData
import kr.kro.btr.utils.restdocs.ARRAY
import kr.kro.btr.utils.restdocs.NUMBER
import kr.kro.btr.utils.restdocs.RestDocsField
import kr.kro.btr.utils.restdocs.STRING
import kr.kro.btr.utils.restdocs.andDocument
import kr.kro.btr.utils.restdocs.isRequired
import kr.kro.btr.utils.restdocs.pathParameters
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
        val crewResult = CrewResult(
            id = 0,
            name = "crewName",
            contents = "contents",
            region = "region",
            imageUri = "imageUri",
            logoUri = "logoUri",
            sns = "crewSnsUri"
        )
        val crews = listOf(crewResult)
        val response = SearchCrewsResponse(listOf(SearchCrewsResponse.Detail(
            id = crewResult.id,
            crewName = crewResult.name,
            contents = crewResult.contents,
            region = crewResult.region,
            imageUri = crewResult.imageUri,
            logoUri = crewResult.logoUri,
            crewSnsUri = crewResult.sns
        )))

        context("목록 조회를 하면") {
            val request = request(HttpMethod.GET, url)
                .contentType(APPLICATION_JSON)

            it("200 OK") {
                every { proxy.searchAll() } returns crews

                mockMvc.perform(request)
                    .andExpect(status().isOk)
                    .andExpectData(
                        jsonPath("$.details[0].id") shouldBe response.details[0].id,
                        jsonPath("$.details[0].crewName") shouldBe response.details[0].crewName,
                        jsonPath("$.details[0].contents") shouldBe response.details[0].contents,
                        jsonPath("$.details[0].region") shouldBe response.details[0].region,
                        jsonPath("$.details[0].imageUri") shouldBe response.details[0].imageUri,
                        jsonPath("$.details[0].logoUri") shouldBe response.details[0].logoUri,
                        jsonPath("$.details[0].crewSnsUri") shouldBe response.details[0].crewSnsUri
                    )
                    .andDocument(
                        "search-crews",
                        responseBody(
                            "details" type ARRAY means "등록된 크루 목록" isRequired true
                        )
                            .andWithPrefix("details[].", getCrewDetailsResponseSnippet())
                    )
            }
        }
    }

    describe("GET : $baseUrl/{crewId}") {
        val url = "$baseUrl/{crewId}"
        val crewId = 0L
        val crewResult = CrewResult(
            id = 0,
            name = "crewName",
            contents = "contents",
            region = "region",
            imageUri = "imageUri",
            logoUri = "logoUri",
            sns = "crewSnsUri"
        )
        val response = DetailCrewResponse(
            id = crewResult.id,
            crewName = crewResult.name,
            contents = crewResult.contents,
            region = crewResult.region,
            imageUri = crewResult.imageUri,
            logoUri = crewResult.logoUri,
            crewSnsUri = crewResult.sns
        )

        context("조회를 하면") {
            val request = request(HttpMethod.GET, url, crewId)
                .contentType(APPLICATION_JSON)

            it("200 OK") {
                every { proxy.detail(any()) } returns crewResult

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
                        pathParameters(
                            "crewId" isRequired true pathMeans "식별자"
                        ),
                        responseBody(
                            "id" type NUMBER means "식별자" isRequired true,
                            "crewName" type STRING means "크루명" isRequired true,
                            "contents" type STRING means "크루 소개" isRequired true,
                            "region" type STRING means "크루 활동 지역" isRequired true,
                            "imageUri" type STRING means "크루 대표 이미지 uri" isRequired false,
                            "logoUri" type STRING means "크루 로고 uri" isRequired false,
                            "crewSnsUri" type STRING means "크루 sns uri" isRequired false
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
                            "name" type STRING means "크루명" isRequired true,
                            "contents" type STRING means "크루소개" isRequired true,
                            "sns" type STRING means "크루 sns uri" isRequired false,
                            "region" type STRING means "크루 활동 지역" isRequired true
                        )
                    )
            }
        }
    }
}) {
    companion object {
        fun getCrewDetailsResponseSnippet(): List<FieldDescriptor> {
            return descriptor(
                "id" type NUMBER means "식별자" isRequired true,
                "crewName" type STRING means "크루명" isRequired true,
                "contents" type STRING means "크루 소개" isRequired true,
                "region" type STRING means "크루 활동 지역" isRequired true,
                "imageUri" type STRING means "크루 대표 이미지 uri" isRequired false,
                "logoUri" type STRING means "크루 로고 uri" isRequired false,
                "crewSnsUri" type STRING means "크루 sns uri" isRequired false
            )
        }

        private fun descriptor(vararg fields: RestDocsField): List<FieldDescriptor> {
            return fields.map { it.descriptor }
        }
    }
}
