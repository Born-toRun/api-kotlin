package kr.kro.btr.adapter.`in`.web

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import kr.kro.btr.adapter.`in`.web.payload.AttendanceActivityRequest
import kr.kro.btr.adapter.`in`.web.payload.CreateActivityRequest
import kr.kro.btr.adapter.`in`.web.payload.ModifyActivityRequest
import kr.kro.btr.adapter.`in`.web.payload.OpenActivityResponse
import kr.kro.btr.adapter.`in`.web.payload.ParticipationActivityResponse
import kr.kro.btr.adapter.`in`.web.payload.DetailActivityResponse
import kr.kro.btr.adapter.`in`.web.payload.SearchActivitiesResponse
import kr.kro.btr.adapter.`in`.web.payload.SearchActivitiesRequest
import kr.kro.btr.adapter.`in`.web.proxy.ActivityProxy
import kr.kro.btr.common.base.ControllerDescribeSpec
import kr.kro.btr.domain.constant.ActivityRecruitmentType
import kr.kro.btr.domain.port.model.result.ActivityResult
import kr.kro.btr.domain.port.model.result.ParticipantResult
import kr.kro.btr.utils.andExpectData
import kr.kro.btr.utils.restdocs.ARRAY
import kr.kro.btr.utils.restdocs.BOOLEAN
import kr.kro.btr.utils.restdocs.DATETIME
import kr.kro.btr.utils.restdocs.NUMBER
import kr.kro.btr.utils.restdocs.OBJECT
import kr.kro.btr.utils.restdocs.RestDocsField
import kr.kro.btr.utils.restdocs.STRING
import kr.kro.btr.utils.restdocs.andDocument
import kr.kro.btr.utils.restdocs.isRequired
import kr.kro.btr.utils.restdocs.pathParameters
import kr.kro.btr.utils.restdocs.queryParameters
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
import java.time.LocalDateTime

@WebMvcTest(ActivityController::class)
class ActivityControllerTest (
    @MockkBean
    private val proxy: ActivityProxy,
    @Autowired
    private val context: WebApplicationContext
): ControllerDescribeSpec ({

    val baseUrl = "/api/v1/activities"
    val restDocumentation = ManualRestDocumentation()
    val mockMvc = restDocMockMvcBuild(context, restDocumentation)

    beforeEach { restDocumentation.beforeTest(javaClass, it.name.testName) }
    afterEach { restDocumentation.afterTest() }

    describe("POST : $baseUrl") {
        val url = baseUrl
        val requestBody = CreateActivityRequest(
            title = "title",
            contents = "contents",
            startAt = LocalDateTime.now(),
            venue = "venue",
            venueUrl = "venueUrl",
            participantsLimit = 0,
            participationFee = 0,
            course = "course",
            courseDetail = "courseDetail",
            path = "path"
        )

        context("등록을 하면") {
            val requestJson = toJson(requestBody)
            val request = request(HttpMethod.POST, url)
                .contentType(APPLICATION_JSON)
                .content(requestJson)

            it("201 Created") {
                every { proxy.create(any(), any()) } just runs

                mockMvc.perform(request)
                    .andExpect(status().isCreated)
                    .andDocument(
                        "create-activities",
                        requestBody(
                            "title" type STRING means "행사명" isRequired true,
                            "contents" type STRING means "내용" isRequired true,
                            "startAt" type DATETIME means "시작일자" isRequired true,
                            "venue" type STRING means "장소" isRequired true,
                            "venueUrl" type STRING means "장소의 지도 url" isRequired true,
                            "participantsLimit" type NUMBER means "인원제한 (제한없음: -1)" isRequired true,
                            "participationFee" type NUMBER means "참가비" isRequired true,
                            "course" type STRING means "코스" isRequired false,
                            "courseDetail" type STRING means "코스 설명" isRequired false,
                            "path" type STRING means "경로" isRequired false
                        )
                    )
            }
        }
    }

    describe("PUT : $baseUrl/{activityId}") {
        val url = "$baseUrl/{activityId}"
        val activityId = 0L
        val requestBody = ModifyActivityRequest(
            title = "title",
            contents = "contents",
            startAt = LocalDateTime.now(),
            venue = "venue",
            venueUrl = "venueUrl",
            participantsLimit = 0,
            participationFee = 0,
            course = "course",
            courseDetail = "courseDetail",
            path = "path"
        )

        context("수정을 하면") {
            val requestJson = toJson(requestBody)
            val request = request(HttpMethod.PUT, url, activityId)
                .contentType(APPLICATION_JSON)
                .content(requestJson)

            it("200 OK") {
                every { proxy.modify(any(), any()) } just runs

                mockMvc.perform(request)
                    .andExpect(status().isOk)
                    .andDocument(
                        "modify-activities",
                        pathParameters(
                            "activityId" isRequired true pathMeans "식별자"
                        ),
                        requestBody(
                            "title" type STRING means "행사명" isRequired true,
                            "contents" type STRING means "내용" isRequired true,
                            "startAt" type DATETIME means "시작일자" isRequired true,
                            "venue" type STRING means "장소" isRequired true,
                            "venueUrl" type STRING means "장소의 지도 url" isRequired true,
                            "participantsLimit" type NUMBER means "인원제한 (제한없음: -1)" isRequired true,
                            "participationFee" type NUMBER means "참가비" isRequired true,
                            "course" type STRING means "코스" isRequired false,
                            "courseDetail" type STRING means "코스 설명" isRequired false,
                            "path" type STRING means "경로" isRequired false
                        )
                    )
            }
        }
    }

    describe("DELETE : $baseUrl/{activityId}") {
        val url = "$baseUrl/{activityId}"
        val activityId = 0L

        context("삭제를 하면") {
            val request = request(HttpMethod.DELETE, url, activityId)
                .contentType(APPLICATION_JSON)

            it("200 OK") {
                every { proxy.remove(any()) } just runs

                mockMvc.perform(request)
                    .andExpect(status().isOk)
                    .andDocument(
                        "remove-activities",
                        pathParameters(
                            "activityId" isRequired true pathMeans "식별자"
                        )
                    )
            }
        }
    }

    describe("POST : $baseUrl/participation/{activityId}") {
        val url = "$baseUrl/participation/{activityId}"
        val activityId = 0L

        context("참여를 하면") {
            val request = request(HttpMethod.POST, url, activityId)
                .contentType(APPLICATION_JSON)

            it("201 Created") {
                every { proxy.participate(any(), any()) } just runs

                mockMvc.perform(request)
                    .andExpect(status().isCreated)
                    .andDocument(
                        "participate-activities",
                        pathParameters(
                            "activityId" isRequired true pathMeans "식별자"
                        )
                    )
            }
        }
    }

    describe("POST : $baseUrl/participation-cancel/{activityId}") {
        val url = "$baseUrl/participation-cancel/{activityId}"
        val activityId = 0L

        context("참여를 취소 하면") {
            val request = request(HttpMethod.POST, url, activityId)
                .contentType(APPLICATION_JSON)

            it("201 Created") {
                every { proxy.participateCancel(any()) } just runs

                mockMvc.perform(request)
                    .andExpect(status().isCreated)
                    .andDocument(
                        "cancel-participate-activities",
                        pathParameters(
                            "activityId" isRequired true pathMeans "식별자"
                        )
                    )
            }
        }
    }

    describe("GET : $baseUrl") {
        val url = baseUrl
        val queryParam = SearchActivitiesRequest(
            courses = listOf("course1", "course2"),
            recruitmentType = ActivityRecruitmentType.RECRUITING
        )
        val activityResults = listOf(
            ActivityResult(
                id = 0,
                title = "title",
                contents = "contents",
                startAt = LocalDateTime.now(),
                venue = "venue",
                venueUrl = "venueUrl",
                participantsLimit = 1,
                participantsQty = 1,
                participationFee = 0,
                course = "course",
                courseDetail = "courseDetail",
                path = "path",
                isOpen = true,
                updatedAt = LocalDateTime.now(),
                registeredAt = LocalDateTime.now(),
                recruitmentType = queryParam.recruitmentType,
                host = ActivityResult.Host(
                    userId = 0,
                    crewId = 0,
                    userProfileUri = "userProfileUri",
                    userName = "userName",
                    crewName = "crewName",
                    isManager = true,
                    isAdmin = false
                ),
            )
        )
        val response = SearchActivitiesResponse(
            activities = listOf(
                SearchActivitiesResponse.Activity(
                    id = activityResults[0].id,
                    title = activityResults[0].title,
                    host = SearchActivitiesResponse.Host(
                        userId = activityResults[0].host.userId,
                        crewId = activityResults[0].host.crewId,
                        userProfileUri = activityResults[0].host.userProfileUri,
                        userName = activityResults[0].host.userName,
                        crewName = activityResults[0].host.crewName,
                        isManager = activityResults[0].host.isManager,
                        isAdmin = activityResults[0].host.isAdmin
                    ),
                    startAt = getDateTimeByFormat(activityResults[0].startAt),
                    course = activityResults[0].course,
                    participantsLimit = activityResults[0].participantsLimit,
                    participantsQty = activityResults[0].participantsQty,
                    updatedAt = getDateTimeByFormat(activityResults[0].updatedAt),
                    registeredAt = getDateTimeByFormat(activityResults[0].registeredAt),
                    isOpen = activityResults[0].isOpen,
                    recruitmentType = activityResults[0].recruitmentType
                )
            )
        )

        context("조회를 하면") {
            val request = request(HttpMethod.GET, url)
                .contentType(APPLICATION_JSON)
                .param("courses", queryParam.courses?.joinToString(","))
                .param("recruitmentType", queryParam.recruitmentType?.name)

            it("200 OK") {
                every { proxy.searchAll(any(), any()) } returns activityResults

                mockMvc.perform(request)
                    .andExpect(status().isOk)
                    .andExpectData(
                        jsonPath("$.activities[0].id") shouldBe response.activities[0].id,
                        jsonPath("$.activities[0].title") shouldBe response.activities[0].title,
                        jsonPath("$.activities[0].startAt") shouldBe response.activities[0].startAt,
                        jsonPath("$.activities[0].course") shouldBe response.activities[0].course,
                        jsonPath("$.activities[0].participantsLimit") shouldBe response.activities[0].participantsLimit,
                        jsonPath("$.activities[0].participantsQty") shouldBe response.activities[0].participantsQty,
                        jsonPath("$.activities[0].updatedAt") shouldBe response.activities[0].updatedAt,
                        jsonPath("$.activities[0].registeredAt") shouldBe response.activities[0].registeredAt,
                        jsonPath("$.activities[0].isOpen") shouldBe response.activities[0].isOpen,
                        jsonPath("$.activities[0].recruitmentType") shouldBe response.activities[0].recruitmentType,
                        jsonPath("$.activities[0].host.userId") shouldBe response.activities[0].host.userId,
                        jsonPath("$.activities[0].host.crewId") shouldBe response.activities[0].host.crewId,
                        jsonPath("$.activities[0].host.userProfileUri") shouldBe response.activities[0].host.userProfileUri,
                        jsonPath("$.activities[0].host.userName") shouldBe response.activities[0].host.userName,
                        jsonPath("$.activities[0].host.crewName") shouldBe response.activities[0].host.crewName,
                        jsonPath("$.activities[0].host.isManager") shouldBe response.activities[0].host.isManager,
                        jsonPath("$.activities[0].host.isAdmin") shouldBe response.activities[0].host.isAdmin,
                    )
                    .andDocument(
                        "search-activities",
                        queryParameters(
                            "courses" isRequired false pathMeans "코스 리스트",
                            "recruitmentType" isRequired false pathMeans "상태"
                        ),
                        responseBody(
                            "activities" type ARRAY means "행사 목록" isRequired true,
                        )
                            .andWithPrefix("activities[]", getActivitiesResponseSnippet())
                    )
            }
        }
    }

    describe("GET : $baseUrl/{activityId}") {
        val url = "$baseUrl/{activityId}"
        val activityId = 0L
        val activityResult = ActivityResult(
            id = 0,
            title = "title",
            contents = "contents",
            startAt = LocalDateTime.now(),
            venue = "venue",
            venueUrl = "venueUrl",
            participantsLimit = 1,
            participantsQty = 1,
            participationFee = 0,
            course = "course",
            courseDetail = "courseDetail",
            path = "path",
            isOpen = true,
            updatedAt = LocalDateTime.now(),
            registeredAt = LocalDateTime.now(),
            recruitmentType = ActivityRecruitmentType.RECRUITING,
            host = ActivityResult.Host(
                userId = 0,
                crewId = 0,
                userProfileUri = "userProfileUri",
                userName = "userName",
                crewName = "crewName",
                isManager = true,
                isAdmin = false
            ),
        )
        val response = DetailActivityResponse(
            id = activityResult.id,
            title = activityResult.title,
            contents = activityResult.contents,
            startAt = getDateTimeByFormat(activityResult.startAt),
            venue = activityResult.venue,
            venueUrl = activityResult.venueUrl,
            participantsLimit = activityResult.participantsLimit,
            participantsQty = activityResult.participantsQty,
            participationFee = activityResult.participationFee,
            course = activityResult.course,
            courseDetail = activityResult.courseDetail,
            path = activityResult.path,
            host = DetailActivityResponse.Host(
                userId = activityResult.host.userId,
                crewId = activityResult.host.crewId,
                userProfileUri = activityResult.host.userProfileUri,
                userName = activityResult.host.userName,
                crewName = activityResult.host.crewName,
                isManager = activityResult.host.isManager,
                isAdmin = activityResult.host.isAdmin
            ),
            isOpen = activityResult.isOpen,
            updatedAt = getDateTimeByFormat(activityResult.updatedAt),
            registeredAt = getDateTimeByFormat(activityResult.registeredAt),
        )

        context("조회를 하면") {
            val request = request(HttpMethod.GET, url, activityId)
                .contentType(APPLICATION_JSON)

            it("200 OK") {
                every { proxy.search(any(), any()) } returns activityResult

                mockMvc.perform(request)
                    .andExpect(status().isOk)
                    .andExpectData(
                        jsonPath("$.id") shouldBe response.id,
                        jsonPath("$.title") shouldBe response.title,
                        jsonPath("$.contents") shouldBe response.contents,
                        jsonPath("$.startAt") shouldBe response.startAt,
                        jsonPath("$.venue") shouldBe response.venue,
                        jsonPath("$.venueUrl") shouldBe response.venueUrl,
                        jsonPath("$.participantsLimit") shouldBe response.participantsLimit,
                        jsonPath("$.participantsQty") shouldBe response.participantsQty,
                        jsonPath("$.participationFee") shouldBe response.participationFee,
                        jsonPath("$.course") shouldBe response.course,
                        jsonPath("$.courseDetail") shouldBe response.courseDetail,
                        jsonPath("$.path") shouldBe response.path,
                        jsonPath("$.isOpen") shouldBe response.isOpen,
                        jsonPath("$.updatedAt") shouldBe response.updatedAt,
                        jsonPath("$.registeredAt") shouldBe response.registeredAt,
                        jsonPath("$.host.userId") shouldBe response.host.userId,
                        jsonPath("$.host.crewId") shouldBe response.host.crewId,
                        jsonPath("$.host.userProfileUri") shouldBe response.host.userProfileUri,
                        jsonPath("$.host.userName") shouldBe response.host.userName,
                        jsonPath("$.host.crewName") shouldBe response.host.crewName,
                        jsonPath("$.host.isManager") shouldBe response.host.isManager,
                        jsonPath("$.host.isAdmin") shouldBe response.host.isAdmin,
                    )
                    .andDocument(
                        "search-activities-detail",
                        pathParameters(
                            "activityId" isRequired true pathMeans "식별자"
                        ),
                        responseBody(
                            "id" type NUMBER means "식별자" isRequired true,
                            "title" type STRING means "행사명" isRequired true,
                            "contents" type STRING means "내용" isRequired true,
                            "startAt" type DATETIME means "시작일자" isRequired true,
                            "venue" type STRING means "장소" isRequired false,
                            "venueUrl" type STRING means "장소의 지도 url" isRequired true,
                            "participantsLimit" type NUMBER means "참여제한" isRequired false,
                            "participantsQty" type NUMBER means "참여자 수" isRequired true,
                            "participationFee" type NUMBER means "참가비" isRequired true,
                            "course" type STRING means "코스" isRequired false,
                            "courseDetail" type STRING means "코스 설명" isRequired false,
                            "path" type STRING means "경로" isRequired false,
                            "isOpen" type BOOLEAN means "오픈 여부" isRequired false,
                            "updatedAt" type DATETIME means "수정일자" isRequired true,
                            "registeredAt" type DATETIME means "등록일자" isRequired true,
                            "host" type OBJECT means "호스트" isRequired true,
                            "host.userId" type NUMBER means "식별자" isRequired true,
                            "host.crewId" type NUMBER means "소속 크루 식별자" isRequired true,
                            "host.userProfileUri" type STRING means "프로필 이미지 uri" isRequired false,
                            "host.userName" type STRING means "유저명" isRequired true,
                            "host.crewName" type STRING means "소속 크루명" isRequired true,
                            "host.isManager" type BOOLEAN means "크루장 여부" isRequired true,
                            "host.isAdmin" type BOOLEAN means "관리자 여부" isRequired true,
                        )
                    )
            }
        }
    }

    describe("PUT : $baseUrl/open/{activityId}") {
        val url = "$baseUrl/open/{activityId}"
        val activityId = 0L
        val activityResult = ActivityResult(
            id = 0,
            title = "title",
            contents = "contents",
            startAt = LocalDateTime.now(),
            venue = "venue",
            venueUrl = "venueUrl",
            participantsLimit = 1,
            participantsQty = 1,
            participationFee = 0,
            course = "course",
            courseDetail = "courseDetail",
            path = "path",
            attendanceCode = 123,
            isOpen = true,
            updatedAt = LocalDateTime.now(),
            registeredAt = LocalDateTime.now(),
            recruitmentType = ActivityRecruitmentType.RECRUITING,
            host = ActivityResult.Host(
                userId = 0,
                crewId = 0,
                userProfileUri = "userProfileUri",
                userName = "userName",
                crewName = "crewName",
                isManager = true,
                isAdmin = false
            ),
        )
        val response = OpenActivityResponse(
            activityId = activityResult.id,
            attendanceCode = activityResult.attendanceCode
        )

        context("오픈을 하면") {
            val request = request(HttpMethod.PUT, url, activityId)
                .contentType(APPLICATION_JSON)

            it("200 OK") {
                every { proxy.open(any()) } returns activityResult

                mockMvc.perform(request)
                    .andExpect(status().isOk)
                    .andExpectData(
                        jsonPath("$.activityId") shouldBe response.activityId,
                        jsonPath("$.attendanceCode") shouldBe response.attendanceCode,
                    )
                    .andDocument(
                        "open-activities",
                        pathParameters(
                            "activityId" isRequired true pathMeans "식별자"
                        ),
                        responseBody(
                            "activityId" type NUMBER means "식별자" isRequired true,
                            "attendanceCode" type NUMBER means "참여코드" isRequired true,
                        )
                    )
            }
        }
    }

    describe("GET : $baseUrl/participation/{activityId}") {
        val url = "$baseUrl/participation/{activityId}"
        val activityId = 0L
        val participantResult = ParticipantResult(
            host = ParticipantResult.Participant(
                userId = 0,
                userName = "userName",
                crewName = "crewName",
                userProfileUri = "userProfileUri"
            ),
            participants = listOf(
                ParticipantResult.Participant(
                    participationId = 0,
                    userId = 0,
                    userName = "userName",
                    crewName = "crewName",
                    userProfileUri = "userProfileUri"
                )
            )
        )
        val response = ParticipationActivityResponse(
            host = ParticipationActivityResponse.Person(
                userId = participantResult.host.userId,
                userName = participantResult.host.userName,
                crewName = participantResult.host.crewName,
                userProfileUri = participantResult.host.userProfileUri
            ),
            participants = listOf(
                ParticipationActivityResponse.Person(
                    participationId = participantResult.participants!![0].participationId,
                    userId = participantResult.participants[0].userId,
                    userName = participantResult.participants[0].userName,
                    crewName = participantResult.participants[0].crewName,
                    userProfileUri = participantResult.participants[0].userProfileUri
                )
            )
        )

        context("조회를 하면") {
            val request = request(HttpMethod.GET, url, activityId)
                .contentType(APPLICATION_JSON)

            it("200 OK") {
                every { proxy.getParticipation(any()) } returns participantResult

                mockMvc.perform(request)
                    .andExpect(status().isOk)
                    .andExpectData(
                        jsonPath("$.host.userId") shouldBe response.host.userId,
                        jsonPath("$.host.userName") shouldBe response.host.userName,
                        jsonPath("$.host.crewName") shouldBe response.host.crewName,
                        jsonPath("$.host.userProfileUri") shouldBe response.host.userProfileUri,
                        jsonPath("$.participants[0].participationId") shouldBe response.participants!![0].participationId,
                        jsonPath("$.participants[0].userId") shouldBe response.participants[0].userId,
                        jsonPath("$.participants[0].userName") shouldBe response.participants[0].userName,
                        jsonPath("$.participants[0].crewName") shouldBe response.participants[0].crewName,
                        jsonPath("$.participants[0].userProfileUri") shouldBe response.participants[0].userProfileUri,
                    )
                    .andDocument(
                        "search-activities-participation",
                        pathParameters(
                            "activityId" isRequired true pathMeans "식별자"
                        ),
                        responseBody(
                            "host" type OBJECT means "호스트" isRequired true,
                            "host.participationId" type NUMBER means "참여 식별자" isRequired false,
                            "host.userId" type NUMBER means "유저 식별자" isRequired true,
                            "host.userName" type STRING means "유저명" isRequired true,
                            "host.crewName" type STRING means "소속 크루명" isRequired true,
                            "host.userProfileUri" type STRING means "프로필 이미지 uri" isRequired false,
                            "participants" type ARRAY means "참여자 목록" isRequired true
                        )
                            .andWithPrefix("participants[]", getParticipantsResponseSnippet())
                    )
            }
        }
    }

    describe("POST : $baseUrl/attendance/{activityId}") {
        val url = "$baseUrl/attendance/{activityId}"
        val activityId = 0L
        val requestBody = AttendanceActivityRequest(
            accessCode = 123
        )

        context("출석을 하면") {
            val requestJson = toJson(requestBody)
            val request = request(HttpMethod.POST, url, activityId)
                .contentType(APPLICATION_JSON)
                .content(requestJson)

            it("200 OK") {
                every { proxy.attendance(any(), any(), any()) } just runs

                mockMvc.perform(request)
                    .andExpect(status().isOk)
                    .andDocument(
                        "activities-attendance",
                        pathParameters(
                            "activityId" isRequired true pathMeans "식별자"
                        ),
                        requestBody(
                            "accessCode" type NUMBER means "참여코드" isRequired true
                        )
                    )
            }
        }
    }
}) {
    companion object {
        fun getParticipantsResponseSnippet(): List<FieldDescriptor> {
            return descriptor(
                "participationId" type NUMBER means "참여 식별자" isRequired false,
                "userId" type NUMBER means "유저 식별자" isRequired true,
                "userName" type STRING means "유저명" isRequired false,
                "crewName" type STRING means "소속 크루명" isRequired false,
                "userProfileUri" type STRING means "프로필 이미지 Uri" isRequired false
            )
        }

        fun getActivitiesResponseSnippet(): List<FieldDescriptor> {
            return descriptor(
                "id" type NUMBER means "식별자" isRequired true,
                "title" type STRING means "행사명" isRequired true,
                "startAt" type DATETIME means "시작일자" isRequired true,
                "course" type STRING means "코스" isRequired false,
                "participantsLimit" type NUMBER means "인원제한" isRequired false,
                "participantsQty" type NUMBER means "참여자 수" isRequired true,
                "updatedAt" type DATETIME means "수정일자" isRequired true,
                "registeredAt" type DATETIME means "등록일자" isRequired true,
                "isOpen" type BOOLEAN means "오픈 여부" isRequired false,
                "recruitmentType" type STRING means "상태" isRequired false,
                "host" type OBJECT means "호스트" isRequired true,
                "host.userId" type NUMBER means "식별자" isRequired true,
                "host.crewId" type NUMBER means "소속 크루 식별자" isRequired false,
                "host.userProfileUri" type STRING means "프로필 이미지 uri" isRequired false,
                "host.userName" type STRING means "유저명" isRequired true,
                "host.crewName" type STRING means "소속 크루명" isRequired false,
                "host.isManager" type BOOLEAN means "크루장 여부" isRequired false,
                "host.isAdmin" type BOOLEAN means "관리자 여부" isRequired false
            )
        }

        private fun descriptor(vararg fields: RestDocsField): List<FieldDescriptor> {
            return fields.map { it.descriptor }
        }
    }
}
