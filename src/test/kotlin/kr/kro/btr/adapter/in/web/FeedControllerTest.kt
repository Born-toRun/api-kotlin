package kr.kro.btr.adapter.`in`.web

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import kr.kro.btr.adapter.`in`.web.payload.CreateFeedRequest
import kr.kro.btr.adapter.`in`.web.payload.DetailFeedResponse
import kr.kro.btr.adapter.`in`.web.payload.DetailFeedResponse.Image
import kr.kro.btr.adapter.`in`.web.payload.ModifyFeedRequest
import kr.kro.btr.adapter.`in`.web.payload.SearchFeedRequest
import kr.kro.btr.adapter.`in`.web.payload.SearchFeedResponse
import kr.kro.btr.adapter.`in`.web.proxy.FeedProxy
import kr.kro.btr.common.base.ControllerDescribeSpec
import kr.kro.btr.core.converter.FeedConverter
import kr.kro.btr.domain.constant.FeedAccessLevel
import kr.kro.btr.domain.constant.FeedCategory
import kr.kro.btr.domain.port.model.FeedCard
import kr.kro.btr.domain.port.model.FeedResult
import kr.kro.btr.utils.andExpectData
import kr.kro.btr.utils.restdocs.ARRAY
import kr.kro.btr.utils.restdocs.BOOLEAN
import kr.kro.btr.utils.restdocs.DATETIME
import kr.kro.btr.utils.restdocs.NUMBER
import kr.kro.btr.utils.restdocs.OBJECT
import kr.kro.btr.utils.restdocs.RestDocsField
import kr.kro.btr.utils.restdocs.STRING
import kr.kro.btr.utils.restdocs.andDocument
import kr.kro.btr.utils.restdocs.pathMeans
import kr.kro.btr.utils.restdocs.pathParameters
import kr.kro.btr.utils.restdocs.requestBody
import kr.kro.btr.utils.restdocs.responseBody
import kr.kro.btr.utils.restdocs.restDocMockMvcBuild
import kr.kro.btr.utils.restdocs.type
import kr.kro.btr.utils.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
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

@WebMvcTest(FeedController::class)
class FeedControllerTest (
    @MockkBean
    private val converter: FeedConverter,
    @MockkBean
    private val proxy: FeedProxy,
    @Autowired
    private val context: WebApplicationContext
): ControllerDescribeSpec ({

    val baseUrl = "/api/v1/feeds"
    val restDocumentation = ManualRestDocumentation()
    val mockMvc = restDocMockMvcBuild(context, restDocumentation)

    beforeEach { restDocumentation.beforeTest(javaClass, it.name.testName) }
    afterEach { restDocumentation.afterTest() }

    describe("GET : $baseUrl") {
        val url = "$baseUrl/{feedId}"
        val feedId = 0L
        val feedResult = FeedResult(
            id = feedId,
            contents = "contents",
            category = FeedCategory.COMMUNITY,
            accessLevel = FeedAccessLevel.ALL,
            viewQty = 0,
            registeredAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            writer = FeedResult.Writer(
                userId = 0,
                userName = "userName",
                crewName = "crewName",
                profileImageUri = "profileImageUri",
                isAdmin = false,
                isManager = true,
            ),
            recommendationQty = 0,
            hasMyRecommendation = true,
            commentQty = 0,
            hasMyComment = false,
        )
        val response = DetailFeedResponse(
            id = feedResult.id,
            contents = feedResult.contents,
            images = feedResult.images?.map { image ->
                Image(
                    imageId = image.id,
                    imageUri = image.imageUri
                )
            },
            category = feedResult.category,
            accessLevel = feedResult.accessLevel,
            viewQty = feedResult.viewQty,
            recommendationQty = feedResult.recommendationQty,
            commentQty = feedResult.commentQty,
            registeredAt = getDateTimeByFormat(feedResult.registeredAt),
            writer = DetailFeedResponse.Writer(
                userId = feedResult.writer.userId,
                userName = feedResult.writer.userName,
                crewName = feedResult.writer.crewName,
                profileImageUri = feedResult.writer.profileImageUri,
                isAdmin = feedResult.writer.isAdmin,
                isManager = feedResult.writer.isManager
            ),
            viewer = DetailFeedResponse.Viewer(
                hasMyRecommendation = feedResult.hasMyRecommendation,
                hasMyComment = feedResult.hasMyComment
            )
        )

        context("조회를 하면") {
            val request = request(HttpMethod.GET, url, feedId)
                .contentType(APPLICATION_JSON)

            it("200 OK") {
                every { proxy.searchDetail(any(), any()) } returns feedResult
                every { proxy.increaseViewQty(any()) } just runs
                every { converter.map(any<FeedResult>()) } returns response

                mockMvc.perform(request)
                    .andExpect(status().isOk)
                    .andExpectData(
                        jsonPath("$.id") shouldBe response.id,
                        jsonPath("$.contents") shouldBe response.contents,
                        jsonPath("$.category") shouldBe response.category,
                        jsonPath("$.accessLevel") shouldBe response.accessLevel,
                        jsonPath("$.viewQty") shouldBe response.viewQty,
                        jsonPath("$.recommendationQty") shouldBe response.recommendationQty,
                        jsonPath("$.commentQty") shouldBe response.commentQty,
                        jsonPath("$.registeredAt") shouldBe response.registeredAt,
                        jsonPath("$.writer.userId") shouldBe response.writer.userId,
                        jsonPath("$.writer.userName") shouldBe response.writer.userName,
                        jsonPath("$.writer.crewName") shouldBe response.writer.crewName,
                        jsonPath("$.writer.profileImageUri") shouldBe response.writer.profileImageUri,
                        jsonPath("$.writer.isAdmin") shouldBe response.writer.isAdmin,
                        jsonPath("$.writer.isManager") shouldBe response.writer.isManager,
                        jsonPath("$.viewer.hasMyRecommendation") shouldBe response.viewer.hasMyRecommendation,
                        jsonPath("$.viewer.hasMyComment") shouldBe response.viewer.hasMyComment
                    )
                    .andDocument(
                        "search-feed-detail",
                        pathParameters(
                            "feedId" pathMeans "식별자"
                        ),
                        responseBody(
                            "id" type NUMBER means "식별자" isRequired true,
                            "contents" type STRING means "내용" isRequired true,
                            "images" type ARRAY means "이미지 목록" isRequired false,
                            "category" type STRING means "카테고리" isRequired true,
                            "accessLevel" type STRING means "공개 여부" isRequired true,
                            "viewQty" type NUMBER means "조회수" isRequired true,
                            "recommendationQty" type NUMBER means "좋아요 수" isRequired true,
                            "commentQty" type NUMBER means "댓글 수" isRequired true,
                            "registeredAt" type DATETIME means "등록일자" isRequired true,
                            "writer" type OBJECT means "작성자" isRequired true,
                            "writer.userId" type NUMBER means "식별자" isRequired true,
                            "writer.userName" type STRING means "유저명" isRequired true,
                            "writer.crewName" type STRING means "소속 크루명" isRequired true,
                            "writer.profileImageUri" type STRING means "프로필 이미지 url" isRequired false,
                            "writer.isAdmin" type BOOLEAN means "관리자 여부" isRequired true,
                            "writer.isManager" type BOOLEAN means "크루장 여부" isRequired true,
                            "viewer" type OBJECT means "조회자" isRequired true,
                            "viewer.hasMyRecommendation" type BOOLEAN means "좋아요 여부" isRequired true,
                            "viewer.hasMyComment" type BOOLEAN means "댓글 여부" isRequired true,
                        )
                            .andWithPrefix("images[]", getImageDetailsResponseSnippet())
                    )
            }
        }
    }

    describe("POST : $baseUrl") {
        val url = baseUrl
        val requestBody = CreateFeedRequest(
            imageIds = listOf(0L, 1L),
            contents = "contents",
            category = FeedCategory.COMMUNITY,
            accessLevel = FeedAccessLevel.ALL
        )

        context("등록을 하면") {
            every { proxy.create(any(), any()) } just runs

            val requestJson = toJson(requestBody)
            val request = request(HttpMethod.POST, url)
                .contentType(APPLICATION_JSON)
                .content(requestJson)

            it("201 Created") {
                mockMvc.perform(request)
                    .andExpect(status().isCreated)
                    .andDocument(
                        "create-feeds",
                        requestBody(
                            "imageIds" type ARRAY means "이미지 식별자 목록" isRequired false,
                            "contents" type STRING means "내용" isRequired true,
                            "category" type STRING means "카테고리" isRequired true,
                            "accessLevel" type STRING means "공개 여부" isRequired false withDefaultValue "ALL",
                        )
                    )
            }
        }
    }

    describe("DELETE : $baseUrl") {
        val url = "$baseUrl/{feedId}"
        val feedId = 0L

        context("삭제를 하면") {
            every { proxy.remove(any(), any()) } just runs

            val request = request(HttpMethod.DELETE, url, feedId)
                .contentType(APPLICATION_JSON)

            it("200 OK") {
                mockMvc.perform(request)
                    .andExpect(status().isOk)
                    .andDocument(
                        "remove-feeds",
                        pathParameters(
                            "feedId" pathMeans "식별자"
                        )
                    )
            }
        }
    }

    describe("PUT : $baseUrl") {
        val url = "$baseUrl/{feedId}"
        val feedId = 0L
        val requestBody = ModifyFeedRequest(
            imageIds = listOf(0L, 1L),
            contents = "contents",
            category = FeedCategory.COMMUNITY,
            accessLevel = FeedAccessLevel.ALL
        )

        context("수정을 하면") {
            every { proxy.modify(any(), any()) } just runs

            val requestJson = toJson(requestBody)
            val request = request(HttpMethod.PUT, url, feedId)
                .contentType(APPLICATION_JSON)
                .content(requestJson)

            it("200 OK") {
                mockMvc.perform(request)
                    .andExpect(status().isOk)
                    .andDocument(
                        "modify-feeds",
                        pathParameters(
                            "feedId" pathMeans "식별자"
                        ),
                        requestBody(
                            "imageIds" type ARRAY means "이미지 식별자 목록" isRequired false,
                            "contents" type STRING means "내용" isRequired true,
                            "category" type STRING means "카테고리" isRequired true,
                            "accessLevel" type STRING means "공개 여부" isRequired true,
                        )
                    )
            }
        }
    }

    describe("GET : $baseUrl") {
        val url = baseUrl
        val requestBody = SearchFeedRequest(
            category = FeedCategory.COMMUNITY,
            searchKeyword = "searchKeyword",
            isMyCrew = false
        )
        val feedCard = FeedCard(
            id = 0,
            contents = "contents",
            viewQty = 0,
            recommendationQty = 0,
            commentQty = 0,
            registeredAt = LocalDateTime.now(),
            writer = FeedCard.Writer(
                userName = "userName",
                crewName = "crewName",
                profileImageUri = "profileImageUri",
                isAdmin = true,
                isManager = false
            ),
            hasRecommendation = true,
            hasComment = true
        )
        val response = SearchFeedResponse(
            id = feedCard.id,
            imageUris = feedCard.imageUris,
            contents = feedCard.contents,
            viewQty = feedCard.viewQty,
            recommendationQty = feedCard.recommendationQty,
            commentQty = feedCard.commentQty,
            registeredAt = getDateTimeByFormat(feedCard.registeredAt),
            writer = SearchFeedResponse.Writer(
                userName = feedCard.writer.userName,
                crewName = feedCard.writer.crewName,
                profileImageUri = feedCard.writer.profileImageUri,
                isAdmin = feedCard.writer.isAdmin,
                isManager = feedCard.writer.isManager
            ),
            viewer = SearchFeedResponse.Viewer(
                hasMyRecommendation = feedCard.hasRecommendation,
                hasMyComment = feedCard.hasComment
            )
        )
        val page = PageImpl<FeedCard>(listOf(feedCard), PageRequest.of(0, 10), 1);

        context("조회를 하면") {
            val requestJson = toJson(requestBody)
            val request = request(HttpMethod.GET, url)
                .contentType(APPLICATION_JSON)
                .content(requestJson)

            it("200 OK") {
                every { proxy.searchAll(any(), any(), any(), any()) } returns page
                every { converter.map(any<FeedCard>()) } returns response

                mockMvc.perform(request)
                    .andExpect(status().isOk)
                    .andExpectData(
                        jsonPath("$.content[0].id") shouldBe response.id,
                        jsonPath("$.content[0].contents") shouldBe response.contents,
                        jsonPath("$.content[0].viewQty") shouldBe response.viewQty,
                        jsonPath("$.content[0].recommendationQty") shouldBe response.recommendationQty,
                        jsonPath("$.content[0].commentQty") shouldBe response.commentQty,
                        jsonPath("$.content[0].registeredAt") shouldBe response.registeredAt,
                        jsonPath("$.content[0].writer.userName") shouldBe response.writer.userName,
                        jsonPath("$.content[0].writer.crewName") shouldBe response.writer.crewName,
                        jsonPath("$.content[0].writer.profileImageUri") shouldBe response.writer.profileImageUri,
                        jsonPath("$.content[0].writer.isAdmin") shouldBe response.writer.isAdmin,
                        jsonPath("$.content[0].writer.isManager") shouldBe response.writer.isManager,
                        jsonPath("$.content[0].viewer.hasMyRecommendation") shouldBe response.viewer.hasMyRecommendation,
                        jsonPath("$.content[0].viewer.hasMyComment") shouldBe response.viewer.hasMyComment
                    )
                    .andDocument(
                        "search-feeds",
                        requestBody(
                            "category" type STRING means "카테고리" isRequired false,
                            "searchKeyword" type STRING means "검색 키워드" isRequired false,
                            "isMyCrew" type BOOLEAN means "나의 크루 보기 여부" isRequired false withDefaultValue "false"
                        ),
                        responseBody(
                            "totalPages" type NUMBER means "총 페이지 수" isRequired true,
                            "totalElements" type NUMBER means "총 피드 개수" isRequired true,
                            "first" type BOOLEAN means "" isRequired true,
                            "last" type BOOLEAN means "" isRequired true,
                            "size" type NUMBER means "" isRequired true,
                            "number" type NUMBER means "" isRequired true,
                            "sort" type OBJECT means "" isRequired true,
                            "sort.empty" type BOOLEAN means "" isRequired true,
                            "sort.sorted" type BOOLEAN means "" isRequired true,
                            "sort.unsorted" type BOOLEAN means "" isRequired true,
                            "pageable" type OBJECT means "" isRequired true,
                            "pageable.pageNumber" type NUMBER means "" isRequired true,
                            "pageable.pageSize" type NUMBER means "" isRequired true,
                            "pageable.sort" type OBJECT means "" isRequired true,
                            "pageable.sort.empty" type BOOLEAN means "" isRequired true,
                            "pageable.sort.sorted" type BOOLEAN means "" isRequired true,
                            "pageable.sort.unsorted" type BOOLEAN means "" isRequired true,
                            "pageable.offset" type NUMBER means "" isRequired true,
                            "pageable.paged" type BOOLEAN means "" isRequired true,
                            "pageable.unpaged" type BOOLEAN means "" isRequired true,
                            "numberOfElements" type NUMBER means "" isRequired true,
                            "empty" type BOOLEAN means "" isRequired true,
                            "content" type ARRAY means "피드 목록" isRequired true,
                        )
                            .andWithPrefix("content[]", getContentResponseSnippet())
                    )
            }
        }
    }
}) {
    companion object {
        fun getImageDetailsResponseSnippet(): List<FieldDescriptor> {
            return descriptor(
                "imageId" type NUMBER means "식별자" isRequired false,
                "imageUrl" type STRING means "이미지 url" isRequired false,
            )
        }

        fun getContentResponseSnippet(): List<FieldDescriptor> {
            return descriptor(
                "id" type NUMBER means "식별자" isRequired true,
                "imageUris" type ARRAY means "이미지 uri 목록" isRequired false,
                "contents" type STRING means "내용" isRequired true,
                "viewQty" type NUMBER means "조회수" isRequired true,
                "recommendationQty" type NUMBER means "좋아요 수" isRequired true,
                "commentQty" type NUMBER means "댓글 수" isRequired true,
                "registeredAt" type DATETIME means "등록일자" isRequired true,
                "writer" type OBJECT means "작성자" isRequired true,
                "writer.userId" type NUMBER means "식별자" isRequired true,
                "writer.userName" type STRING means "유저명" isRequired true,
                "writer.crewName" type STRING means "소속 크루명" isRequired true,
                "writer.profileImageUri" type STRING means "프로필 이미지 url" isRequired false,
                "writer.isAdmin" type BOOLEAN means "관리자 여부" isRequired true,
                "writer.isManager" type BOOLEAN means "크루장 여부" isRequired true,
                "viewer" type OBJECT means "조회자" isRequired true,
                "viewer.hasMyRecommendation" type BOOLEAN means "좋아요 여부" isRequired true,
                "viewer.hasMyComment" type BOOLEAN means "댓글 여부" isRequired true,
            )
        }

        private fun descriptor(vararg fields: RestDocsField): List<FieldDescriptor> {
            return fields.map { it.descriptor }
        }
    }
}
