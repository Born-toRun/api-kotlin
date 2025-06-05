package kr.kro.btr.adapter.`in`.web

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import kr.kro.btr.adapter.`in`.web.payload.CreateCommentRequest
import kr.kro.btr.adapter.`in`.web.payload.ModifyCommentRequest
import kr.kro.btr.adapter.`in`.web.payload.ModifyCommentResponse
import kr.kro.btr.adapter.`in`.web.payload.QtyCommentResponse
import kr.kro.btr.adapter.`in`.web.payload.SearchCommentDetailResponse
import kr.kro.btr.adapter.`in`.web.payload.SearchCommentResponse
import kr.kro.btr.adapter.`in`.web.proxy.CommentProxy
import kr.kro.btr.common.base.ControllerDescribeSpec
import kr.kro.btr.domain.port.model.result.CommentDetailResult
import kr.kro.btr.domain.port.model.result.CommentResult
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
import kotlin.collections.List

@WebMvcTest(CommentController::class)
class CommentControllerTest (
    @MockkBean
    private val proxy: CommentProxy,
    @Autowired
    private val context: WebApplicationContext
): ControllerDescribeSpec ({

    val baseUrl = "/api/v1/comments"
    val restDocumentation = ManualRestDocumentation()
    val mockMvc = restDocMockMvcBuild(context, restDocumentation)

    beforeEach { restDocumentation.beforeTest(javaClass, it.name.testName) }
    afterEach { restDocumentation.afterTest() }

    describe("GET : $baseUrl/{feedId}") {
        val url = "$baseUrl/{feedId}"
        val feedId = 0L
        val commentResults = listOf(
            CommentResult(
                id = 0,
                reCommentQty = 0,
                feedId = 0,
                contents = "contents",
                registeredAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
                isMyComment = true,
                writer = CommentResult.Writer(
                    userId = 0,
                    userName = "userName",
                    profileImageUri = "profileImageUri",
                    crewName = "crewName",
                    isAdmin = true,
                    isManager = false
                ),
            )
        )
        val response = SearchCommentResponse(
            comments = listOf(
                SearchCommentResponse.Comment(
                    id = commentResults[0].id,
                    parentId = commentResults[0].parentId,
                    reCommentQty = commentResults[0].reCommentQty,
                    contents = commentResults[0].contents,
                    registeredAt = getDateTimeByFormat(commentResults[0].registeredAt),
                    isMyComment = commentResults[0].isMyComment,
                    writer = SearchCommentResponse.Writer(
                        userId = commentResults[0].writer.userId,
                        userName = commentResults[0].writer.userName,
                        profileImageUri = commentResults[0].writer.profileImageUri,
                        crewName = commentResults[0].writer.crewName,
                        isAdmin = commentResults[0].writer.isAdmin,
                        isManager = commentResults[0].writer.isManager
                    ),
                )
            )
        )

        context("조회를 하면") {
            val request = request(HttpMethod.GET, url, feedId)
                .contentType(APPLICATION_JSON)

            it("200 OK") {
                every { proxy.searchAll(any(), any()) } returns commentResults

                mockMvc.perform(request)
                    .andExpect(status().isOk)
                    .andExpectData(
                        jsonPath("$.comments[0].id") shouldBe response.comments[0].id,
                        jsonPath("$.comments[0].reCommentQty") shouldBe response.comments[0].reCommentQty,
                        jsonPath("$.comments[0].contents") shouldBe response.comments[0].contents,
                        jsonPath("$.comments[0].registeredAt") shouldBe response.comments[0].registeredAt,
                        jsonPath("$.comments[0].isMyComment") shouldBe response.comments[0].isMyComment,
                        jsonPath("$.comments[0].isReComment") shouldBe response.comments[0].isReComment(),
                        jsonPath("$.comments[0].writer.userId") shouldBe response.comments[0].writer.userId,
                        jsonPath("$.comments[0].writer.userName") shouldBe response.comments[0].writer.userName,
                        jsonPath("$.comments[0].writer.profileImageUri") shouldBe response.comments[0].writer.profileImageUri,
                        jsonPath("$.comments[0].writer.crewName") shouldBe response.comments[0].writer.crewName,
                        jsonPath("$.comments[0].writer.isAdmin") shouldBe response.comments[0].writer.isAdmin,
                        jsonPath("$.comments[0].writer.isManager") shouldBe response.comments[0].writer.isManager,
                    )
                    .andDocument(
                        "search-comments",
                        pathParameters(
                            "feedId" isRequired true pathMeans "조회할 피드 식별자"
                        ),
                        responseBody(
                            "comments" type ARRAY means "댓글 목록" isRequired true,
                        )
                            .andWithPrefix("comments[]", getCommentsResponseSnippet())
                    )
            }
        }
    }

    describe("GET : $baseUrl/detail/{commentId}") {
        val url = "$baseUrl/detail/{commentId}"
        val commentId = 0L
        val commentDetailResult = CommentDetailResult(
            id = 0,
            feedId = 0,
            contents = "contents",
            registeredAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            writer = CommentDetailResult.Writer(
                userId = 0,
                userName = "userName",
                profileImageUri = "profileImageUri",
                crewName = "crewName",
                isAdmin = true,
                isManager = false
            ),
            reCommentResults = listOf(
                CommentResult(
                    id = 0,
                    reCommentQty = 0,
                    feedId = 0,
                    contents = "contents",
                    registeredAt = LocalDateTime.now(),
                    updatedAt = LocalDateTime.now(),
                    isMyComment = true,
                    writer = CommentResult.Writer(
                        userId = 0,
                        userName = "userName",
                        profileImageUri = "profileImageUri",
                        crewName = "crewName",
                        isAdmin = true,
                        isManager = false
                    ),
                )
            )
        )
        val response = SearchCommentDetailResponse(
            id = commentDetailResult.id,
            writer = SearchCommentDetailResponse.Writer(
                userId = commentDetailResult.writer.userId,
                userName = commentDetailResult.writer.userName,
                profileImageUri = commentDetailResult.writer.profileImageUri,
                crewName = commentDetailResult.writer.crewName,
                isAdmin = commentDetailResult.writer.isAdmin,
                isManager = commentDetailResult.writer.isManager,
            ),
            contents = commentDetailResult.contents,
            registeredAt = getDateTimeByFormat(commentDetailResult.registeredAt),
            reComments = listOf(
                SearchCommentDetailResponse.ReComment(
                    id = commentDetailResult.reCommentResults[0].id,
                    contents = commentDetailResult.reCommentResults[0].contents,
                    registeredAt = getDateTimeByFormat(commentDetailResult.reCommentResults[0].registeredAt),
                    isMyComment = commentDetailResult.reCommentResults[0].isMyComment,
                    writer = SearchCommentDetailResponse.ReComment.Writer(
                        userId = commentDetailResult.reCommentResults[0].writer.userId,
                        userName = commentDetailResult.reCommentResults[0].writer.userName,
                        profileImageUri = commentDetailResult.reCommentResults[0].writer.profileImageUri,
                        crewName = commentDetailResult.reCommentResults[0].writer.crewName,
                        isAdmin = commentDetailResult.reCommentResults[0].writer.isAdmin,
                        isManager = commentDetailResult.reCommentResults[0].writer.isManager,
                    )
                )
            )
        )

        context("조회를 하면") {
            val request = request(HttpMethod.GET, url, commentId)
                .contentType(APPLICATION_JSON)

            it("200 OK") {
                every { proxy.detail(any(), any()) } returns commentDetailResult

                mockMvc.perform(request)
                    .andExpect(status().isOk)
                    .andExpectData(
                        jsonPath("$.id") shouldBe response.id,
                        jsonPath("$.contents") shouldBe response.contents,
                        jsonPath("$.registeredAt") shouldBe response.registeredAt,
                        jsonPath("$.writer.userId") shouldBe response.writer.userId,
                        jsonPath("$.writer.userName") shouldBe response.writer.userName,
                        jsonPath("$.writer.profileImageUri") shouldBe response.writer.profileImageUri,
                        jsonPath("$.writer.crewName") shouldBe response.writer.crewName,
                        jsonPath("$.writer.isAdmin") shouldBe response.writer.isAdmin,
                        jsonPath("$.writer.isManager") shouldBe response.writer.isManager,
                        jsonPath("$.reComments[0].id") shouldBe response.reComments[0].id,
                        jsonPath("$.reComments[0].contents") shouldBe response.reComments[0].contents,
                        jsonPath("$.reComments[0].registeredAt") shouldBe response.reComments[0].registeredAt,
                        jsonPath("$.reComments[0].isMyComment") shouldBe response.reComments[0].isMyComment,
                        jsonPath("$.reComments[0].writer.userId") shouldBe response.reComments[0].writer.userId,
                        jsonPath("$.reComments[0].writer.userName") shouldBe response.reComments[0].writer.userName,
                        jsonPath("$.reComments[0].writer.profileImageUri") shouldBe response.reComments[0].writer.profileImageUri,
                        jsonPath("$.reComments[0].writer.crewName") shouldBe response.reComments[0].writer.crewName,
                        jsonPath("$.reComments[0].writer.isAdmin") shouldBe response.reComments[0].writer.isAdmin,
                        jsonPath("$.reComments[0].writer.isManager") shouldBe response.reComments[0].writer.isManager

                    )
                    .andDocument(
                        "search-comment",
                        pathParameters(
                            "commentId" isRequired true pathMeans "조회할 댓글 식별자"
                        ),
                        responseBody(
                            "id" type NUMBER means "식별자" isRequired true,
                            "contents" type STRING means "내용" isRequired true,
                            "registeredAt" type DATETIME means "등록일자" isRequired true,
                            "writer" type OBJECT means "작성자" isRequired true,
                            "writer.userId" type NUMBER means "유저 식별자" isRequired true,
                            "writer.userName" type STRING means "유저명" isRequired true,
                            "writer.profileImageUri" type STRING means "프로필 이미지 uri" isRequired false,
                            "writer.crewName" type STRING means "소속 크루 명" isRequired true,
                            "writer.isAdmin" type BOOLEAN means "관리자 여부" isRequired true,
                            "writer.isManager" type BOOLEAN means "크루장 여부" isRequired true,
                            "reComments" type ARRAY means "대댓글 목록" isRequired true,
                        )
                            .andWithPrefix("reComments[]", getReCommentsResponseSnippet())
                    )
            }
        }
    }

    describe("POST : $baseUrl/{feedId}") {
        val url = "$baseUrl/{feedId}"
        val feedId = 0L
        val requestBody = CreateCommentRequest(
            parentCommentId = 0,
            contents = "contents"
        )

        context("등록을 하면") {
            val requestJson = toJson(requestBody)
            val request = request(HttpMethod.POST, url, feedId)
                .contentType(APPLICATION_JSON)
                .content(requestJson)

            it("201 Created") {
                every { proxy.create(any(), any(), any()) } just runs

                mockMvc.perform(request)
                    .andExpect(status().isCreated)
                    .andDocument(
                        "modify-comments",
                        pathParameters(
                            "feedId" isRequired true pathMeans "피드 식별자"
                        ),
                        requestBody(
                            "parentCommentId" type NUMBER means "부모 댓글 식별자" isRequired false,
                            "contents" type STRING means "내용" isRequired true,
                        )
                    )
            }
        }
    }

    describe("DELETE : $baseUrl/{commentId}") {
        val url = "$baseUrl/{commentId}"
        val commentId = 0L

        context("삭제를 하면") {
            val request = request(HttpMethod.DELETE, url, commentId)
                .contentType(APPLICATION_JSON)

            it("200 OK") {
                every { proxy.remove(any()) } just runs

                mockMvc.perform(request)
                    .andExpect(status().isOk)
                    .andDocument(
                        "remove-comments",
                        pathParameters(
                            "commentId" isRequired true pathMeans "식별자"
                        )
                    )
            }
        }
    }

    describe("PUT : $baseUrl/{commentId}") {
        val url = "$baseUrl/{commentId}"
        val commentId = 0L
        val requestBody = ModifyCommentRequest(
            contents = "contents"
        )
        val commentResult = CommentResult(
            id = 0,
            reCommentQty = 0,
            feedId = 0,
            contents = "contents",
            registeredAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            isMyComment = true,
            writer = CommentResult.Writer(
                userId = 0,
                userName = "userName",
                profileImageUri = "profileImageUri",
                crewName = "crewName",
                isAdmin = true,
                isManager = false
            ),
        )
        val response = ModifyCommentResponse(
            id = commentResult.id,
            contents = commentResult.contents
        )

        context("수정을 하면") {
            val requestJson = toJson(requestBody)
            val request = request(HttpMethod.PUT, url, commentId)
                .contentType(APPLICATION_JSON)
                .content(requestJson)

            it("200 OK") {
                every { proxy.modify(any(), any()) } returns commentResult

                mockMvc.perform(request)
                    .andExpect(status().isOk)
                    .andDocument(
                        "modify-comments",
                        pathParameters(
                            "commentId" isRequired true pathMeans "식별자"
                        ),
                        requestBody(
                            "contents" type STRING means "내용" isRequired true
                        ),
                        responseBody(
                            "id" type NUMBER means "식별자" isRequired true,
                            "contents" type STRING means "내용" isRequired true,
                        )
                    )
            }
        }
    }

    describe("GET : $baseUrl/qty/{feedId}") {
        val url = "$baseUrl/qty/{feedId}"
        val feedId = 0L
        val qty = 0
        val response = QtyCommentResponse(
            qty = qty
        )

        context("수정을 하면") {
            val request = request(HttpMethod.GET, url, feedId)
                .contentType(APPLICATION_JSON)

            it("200 OK") {
                every { proxy.qty(any()) } returns qty

                mockMvc.perform(request)
                    .andExpect(status().isOk)
                    .andExpectData(
                        jsonPath("$.qty") shouldBe response.qty
                    )
                    .andDocument(
                        "qty-comments",
                        pathParameters(
                            "feedId" isRequired true pathMeans "피드 식별자"
                        ),
                        responseBody(
                            "qty" type NUMBER means "댓글 개수" isRequired true,
                        )
                    )
            }
        }
    }
}) {
    companion object {
        fun getReCommentsResponseSnippet(): List<FieldDescriptor> {
            return descriptor(
                "id" type NUMBER means "식별자" isRequired true,
                "contents" type STRING means "내용" isRequired true,
                "registeredAt" type DATETIME means "등록일시" isRequired true,
                "isMyComment" type BOOLEAN means "나의 댓글 여부" isRequired true,
                "writer.userId" type NUMBER means "식별자" isRequired false,
                "writer.userName" type STRING means "유저명" isRequired false,
                "writer.profileImageUri" type STRING means "프로필 이미지 uri" isRequired false,
                "writer.crewName" type STRING means "소속 크루명" isRequired false,
                "writer.isAdmin" type BOOLEAN means "관리자 여부" isRequired false withDefaultValue "false",
                "writer.isManager" type BOOLEAN means "크루장 여부" isRequired false withDefaultValue "false"
            )
        }

        fun getCommentsResponseSnippet(): List<FieldDescriptor> {
            return descriptor(
                "id" type NUMBER means "식별자" isRequired true,
                "parentId" type NUMBER means "부모 댓글 식별자" isRequired false,
                "reCommentQty" type NUMBER means "대댓글 개수" isRequired true,
                "contents" type STRING means "내용" isRequired true,
                "registeredAt" type DATETIME means "등록일자" isRequired true,
                "isMyComment" type BOOLEAN means "나의 댓글 여부" isRequired true,
                "isReComment" type BOOLEAN means "대댓글 여부" isRequired true,
                "writer" type OBJECT means "식별자" isRequired true,
                "writer.userId" type NUMBER means "식별자" isRequired true,
                "writer.userName" type STRING means "유저명" isRequired true,
                "writer.profileImageUri" type STRING means "프로필 이미지 uri" isRequired false,
                "writer.crewName" type STRING means "소속 크루명" isRequired true,
                "writer.isAdmin" type BOOLEAN means "관리자 여부" isRequired true,
                "writer.isManager" type BOOLEAN means "크루장 여부" isRequired true,
            )
        }

        private fun descriptor(vararg fields: RestDocsField): List<FieldDescriptor> {
            return fields.map { it.descriptor }
        }
    }
}
