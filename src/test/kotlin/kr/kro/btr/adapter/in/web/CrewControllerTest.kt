package kr.kro.btr.adapter.`in`.web

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import kr.kro.btr.adapter.`in`.web.payload.CreateCrewRequest
import kr.kro.btr.adapter.`in`.web.payload.DetailCrewResponse
import kr.kro.btr.adapter.`in`.web.payload.ModifyCrewRequest
import kr.kro.btr.adapter.`in`.web.payload.SearchCrewsResponse
import kr.kro.btr.adapter.`in`.web.proxy.CrewProxy
import kr.kro.btr.common.base.ControllerDescribeSpec
import kr.kro.btr.domain.port.model.result.CrewMemberResult
import kr.kro.btr.domain.port.model.result.CrewResult
import kr.kro.btr.utils.andExpectData
import kr.kro.btr.utils.restdocs.ARRAY
import kr.kro.btr.utils.restdocs.BOOLEAN
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

    describe("GET : $baseUrl/my") {
        val url = "$baseUrl/my"
        val crewId = 1L
        val crewResult = CrewResult(
            id = 1,
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

        context("로그인 사용자의 크루 상세 조회를 하면") {
            val request = request(HttpMethod.GET, url)
                .contentType(APPLICATION_JSON)

            it("200 OK") {
                every { proxy.detailMyCrew(any()) } returns crewResult

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
                        "search-my-crew-detail",
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

    describe("GET : $baseUrl/{crewId}/members") {
        val url = "$baseUrl/{crewId}/members"
        val crewId = 1L
        val memberResults = listOf(
            CrewMemberResult(
                userId = 1,
                userName = "홍길동",
                profileImageUri = "https://example.com/profile1.jpg",
                instagramId = "@hong123",
                isManager = true,
                isAdmin = false
            ),
            CrewMemberResult(
                userId = 2,
                userName = "김철수",
                profileImageUri = "https://example.com/profile2.jpg",
                instagramId = "@kim456",
                isManager = false,
                isAdmin = false
            )
        )

        context("크루 멤버 목록 조회를 하면") {
            val request = request(HttpMethod.GET, url, crewId)
                .contentType(APPLICATION_JSON)

            it("200 OK") {
                every { proxy.searchMembers(any()) } returns memberResults

                mockMvc.perform(request)
                    .andExpect(status().isOk)
                    .andExpectData(
                        jsonPath("$.members[0].userId") shouldBe memberResults[0].userId,
                        jsonPath("$.members[0].userName") shouldBe memberResults[0].userName,
                        jsonPath("$.members[0].profileImageUri") shouldBe memberResults[0].profileImageUri,
                        jsonPath("$.members[0].instagramId") shouldBe memberResults[0].instagramId,
                        jsonPath("$.members[0].isManager") shouldBe memberResults[0].isManager,
                        jsonPath("$.members[0].isAdmin") shouldBe memberResults[0].isAdmin,
                        jsonPath("$.members[1].userId") shouldBe memberResults[1].userId,
                        jsonPath("$.members[1].userName") shouldBe memberResults[1].userName,
                        jsonPath("$.members[1].isManager") shouldBe memberResults[1].isManager,
                        jsonPath("$.members[1].isAdmin") shouldBe memberResults[1].isAdmin
                    )
                    .andDocument(
                        "search-crew-members",
                        pathParameters(
                            "crewId" isRequired true pathMeans "크루 식별자"
                        ),
                        responseBody(
                            "members" type ARRAY means "크루 멤버 목록" isRequired true
                        )
                            .andWithPrefix("members[].", getCrewMembersResponseSnippet())
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

    describe("PUT : $baseUrl/{crewId}") {
        val url = "$baseUrl/{crewId}"
        val crewId = 1L
        val requestBody = ModifyCrewRequest(
            name = "Updated Crew Name",
            contents = "Updated crew description",
            sns = "https://instagram.com/updated",
            region = "Updated Region",
            imageId = 100L,
            logoId = 200L
        )

        context("수정을 하면") {
            every { proxy.modify(any(), any()) } just runs

            val requestJson = toJson(requestBody)
            val request = request(HttpMethod.PUT, url, crewId)
                .contentType(APPLICATION_JSON)
                .content(requestJson)

            it("200 OK") {
                mockMvc.perform(request)
                    .andExpect(status().isOk)
                    .andDocument(
                        "modify-crews",
                        pathParameters(
                            "crewId" isRequired true pathMeans "크루 식별자"
                        ),
                        requestBody(
                            "name" type STRING means "크루명" isRequired true,
                            "contents" type STRING means "크루소개" isRequired true,
                            "sns" type STRING means "크루 sns uri" isRequired false,
                            "region" type STRING means "크루 활동 지역" isRequired true,
                            "imageId" type NUMBER means "대표 이미지 ID" isRequired false,
                            "logoId" type NUMBER means "로고 이미지 ID" isRequired false
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

        fun getCrewMembersResponseSnippet(): List<FieldDescriptor> {
            return descriptor(
                "userId" type NUMBER means "사용자 식별자" isRequired true,
                "userName" type STRING means "사용자 이름" isRequired true,
                "profileImageUri" type STRING means "프로필 이미지 URI" isRequired false,
                "instagramId" type STRING means "인스타그램 ID" isRequired false,
                "isManager" type BOOLEAN means "매니저 여부" isRequired true,
                "isAdmin" type BOOLEAN means "관리자 여부" isRequired true
            )
        }

        private fun descriptor(vararg fields: RestDocsField): List<FieldDescriptor> {
            return fields.map { it.descriptor }
        }
    }
}
