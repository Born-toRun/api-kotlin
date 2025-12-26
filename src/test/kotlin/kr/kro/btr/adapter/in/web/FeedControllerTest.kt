package kr.kro.btr.adapter.`in`.web

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import kr.kro.btr.adapter.`in`.web.payload.*
import kr.kro.btr.adapter.`in`.web.payload.DetailFeedResponse.Image
import kr.kro.btr.adapter.`in`.web.proxy.FeedProxy
import kr.kro.btr.common.base.ControllerDescribeSpec
import kr.kro.btr.domain.constant.FeedAccessLevel
import kr.kro.btr.domain.constant.FeedCategory
import kr.kro.btr.domain.port.model.result.FeedDetailResult
import kr.kro.btr.domain.port.model.result.FeedResult
import kr.kro.btr.utils.andExpectData
import kr.kro.btr.utils.restdocs.*
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

@WebMvcTest(FeedController::class)
class FeedControllerTest (
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
        val feedDetailResult = FeedDetailResult(
            id = feedId,
            contents = "contents",
            category = FeedCategory.COMMUNITY,
            accessLevel = FeedAccessLevel.ALL,
            viewQty = 0,
            registeredAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            writer = FeedDetailResult.Writer(
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
            id = feedDetailResult.id,
            contents = feedDetailResult.contents,
            images = feedDetailResult.images?.map { image ->
                Image(
                    imageId = image.id,
                    imageUri = image.imageUri
                )
            },
            category = feedDetailResult.category,
            accessLevel = feedDetailResult.accessLevel,
            viewQty = feedDetailResult.viewQty,
            recommendationQty = feedDetailResult.recommendationQty,
            commentQty = feedDetailResult.commentQty,
            registeredAt = getDateTimeByFormat(feedDetailResult.registeredAt),
            writer = DetailFeedResponse.Writer(
                userId = feedDetailResult.writer.userId,
                userName = feedDetailResult.writer.userName,
                crewName = feedDetailResult.writer.crewName,
                profileImageUri = feedDetailResult.writer.profileImageUri,
                isAdmin = feedDetailResult.writer.isAdmin,
                isManager = feedDetailResult.writer.isManager
            ),
            viewer = DetailFeedResponse.Viewer(
                hasMyRecommendation = feedDetailResult.hasMyRecommendation,
                hasMyComment = feedDetailResult.hasMyComment
            )
        )

        context("조회를 하면") {
            val request = request(HttpMethod.GET, url, feedId)
                .contentType(APPLICATION_JSON)

            it("200 OK") {
                every { proxy.searchDetail(any(), any()) } returns feedDetailResult
                every { proxy.increaseViewQty(any()) } just runs

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
                            "feedId" isRequired true pathMeans "식별자"
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
                            "feedId" isRequired true pathMeans "식별자"
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
                            "feedId" isRequired true pathMeans "식별자"
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
        val queryParam = SearchFeedRequest(
            category = FeedCategory.COMMUNITY,
            searchKeyword = "searchKeyword",
            isMyCrew = false
        )
        val feedResult = FeedResult(
            id = 0,
            contents = "contents",
            viewQty = 0,
            recommendationQty = 0,
            commentQty = 0,
            registeredAt = LocalDateTime.now(),
            writer = FeedResult.Writer(
                userName = "userName",
                crewName = "crewName",
                profileImageUri = "profileImageUri",
                isAdmin = true,
                isManager = false
            ),
            hasRecommendation = true,
            hasComment = true
        )
        val response = SearchFeedsResponse(
            id = feedResult.id,
            imageUris = feedResult.imageUris,
            contents = feedResult.contents,
            viewQty = feedResult.viewQty,
            recommendationQty = feedResult.recommendationQty,
            commentQty = feedResult.commentQty,
            registeredAt = getDateTimeByFormat(feedResult.registeredAt),
            writer = SearchFeedsResponse.Writer(
                userName = feedResult.writer.userName,
                crewName = feedResult.writer.crewName,
                profileImageUri = feedResult.writer.profileImageUri,
                isAdmin = feedResult.writer.isAdmin,
                isManager = feedResult.writer.isManager
            ),
            viewer = SearchFeedsResponse.Viewer(
                hasMyRecommendation = feedResult.hasRecommendation,
                hasMyComment = feedResult.hasComment
            )
        )
        val page = PageImpl<FeedResult>(listOf(feedResult), PageRequest.of(0, 10), 1);

        context("조회를 하면") {
            val request = request(HttpMethod.GET, url)
                .contentType(APPLICATION_JSON)
                .param("isMyCrew", queryParam.isMyCrew.toString())
                .param("searchKeyword", queryParam.searchKeyword)
                .param("category", queryParam.category?.name)
                .param("lastFeedId", "0")
                .param("size", "10")

            it("200 OK") {
                every { proxy.searchAll(any(), any(), any(), any()) } returns page

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
                        queryParameters(
                            "lastFeedId" isRequired false pathMeans "이전 페이지의 마지막 피드 식별자" default "0",
                            "size" isRequired false pathMeans "페이징 사이즈" default "10",
                            "category" isRequired false pathMeans "카테고리",
                            "searchKeyword" isRequired false pathMeans "검색 키워드",
                            "isMyCrew" isRequired false pathMeans "나의 크루 보기 여부"
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

    describe("GET : $baseUrl/my") {
        val url = "$baseUrl/my"
        val feedResults = listOf(
            FeedResult(
                id = 1L,
                imageUris = listOf("https://example.com/image1.jpg", "https://example.com/image2.jpg"),
                contents = "내가 작성한 첫 번째 피드\n이것은 추가적인 내용입니다.",
                viewQty = 10,
                recommendationQty = 5,
                commentQty = 3,
                registeredAt = LocalDateTime.of(2024, 3, 15, 19, 0),
                writer = FeedResult.Writer(
                    userName = "userName",
                    crewName = "crewName",
                    profileImageUri = "profileImageUri",
                    isAdmin = false,
                    isManager = true
                ),
                hasRecommendation = true,
                hasComment = false
            ),
            FeedResult(
                id = 2L,
                imageUris = emptyList(),
                contents = "내가 작성한 두 번째 피드",
                viewQty = 20,
                recommendationQty = 10,
                commentQty = 7,
                registeredAt = LocalDateTime.of(2024, 4, 20, 7, 0),
                writer = FeedResult.Writer(
                    userName = "userName",
                    crewName = "crewName",
                    profileImageUri = "profileImageUri",
                    isAdmin = false,
                    isManager = true
                ),
                hasRecommendation = false,
                hasComment = true
            )
        )

        context("나의 피드 목록을 조회하면") {
            val request = request(HttpMethod.GET, url)
                .contentType(APPLICATION_JSON)

            it("200 OK") {
                every { proxy.searchMyFeeds(any()) } returns feedResults

                mockMvc.perform(request)
                    .andExpect(status().isOk)
                    .andExpectData(
                        jsonPath("$.feeds[0].feedId") shouldBe feedResults[0].id,
                        jsonPath("$.feeds[0].contents") shouldBe feedResults[0].contents,
                        jsonPath("$.feeds[0].imageUris[0]") shouldBe "https://example.com/image1.jpg",
                        jsonPath("$.feeds[0].imageUris[1]") shouldBe "https://example.com/image2.jpg",
                        jsonPath("$.feeds[1].feedId") shouldBe feedResults[1].id,
                        jsonPath("$.feeds[1].contents") shouldBe feedResults[1].contents,
                        jsonPath("$.feeds[1].imageUris").isEmpty
                    )
                    .andDocument(
                        "search-my-feeds",
                        responseBody(
                            "feeds" type ARRAY means "피드 목록" isRequired true,
                            "feeds[].feedId" type NUMBER means "피드 식별자" isRequired true,
                            "feeds[].contents" type STRING means "피드 내용" isRequired true,
                            "feeds[].imageUris" type ARRAY means "첨부 이미지 URI 목록" isRequired true
                        )
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
