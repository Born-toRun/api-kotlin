package kr.kro.btr.adapter.`in`.web

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import kr.kro.btr.adapter.`in`.web.payload.UploadFileResponse
import kr.kro.btr.adapter.`in`.web.proxy.ObjectStorageProxy
import kr.kro.btr.common.base.ControllerDescribeSpec
import kr.kro.btr.domain.constant.Bucket
import kr.kro.btr.domain.port.model.ObjectStorage
import kr.kro.btr.utils.CommonTestFixture.getMultipartFile
import kr.kro.btr.utils.andExpectData
import kr.kro.btr.utils.restdocs.NUMBER
import kr.kro.btr.utils.restdocs.STRING
import kr.kro.btr.utils.restdocs.andDocument
import kr.kro.btr.utils.restdocs.isRequired
import kr.kro.btr.utils.restdocs.pathParameters
import kr.kro.btr.utils.restdocs.responseBody
import kr.kro.btr.utils.restdocs.restDocMockMvcBuild
import kr.kro.btr.utils.restdocs.type
import kr.kro.btr.utils.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.http.MediaType.MULTIPART_FORM_DATA
import org.springframework.restdocs.ManualRestDocumentation
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.multipart
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.request
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.web.context.WebApplicationContext
import java.time.LocalDateTime

@WebMvcTest(ObjectStorageController::class)
class ObjectStorageControllerTest (
    @MockkBean
    private val proxy: ObjectStorageProxy,
    @Autowired
    private val context: WebApplicationContext
): ControllerDescribeSpec ({

    val baseUrl = "/api/v1/object-storage"
    val restDocumentation = ManualRestDocumentation()
    val mockMvc = restDocMockMvcBuild(context, restDocumentation)

    beforeEach { restDocumentation.beforeTest(javaClass, it.name.testName) }
    afterEach { restDocumentation.afterTest() }

    describe("POST : $baseUrl/{bucket}") {
        val url = "$baseUrl/{bucket}"
        val bucket = Bucket.FEED.name
        val file = getMultipartFile()
        val objectStorage = ObjectStorage(
            id = 0,
            userId = 0,
            fileUri = "www.file.uri",
            uploadAt = LocalDateTime.now()
        )
        val response = UploadFileResponse(
            fileId = objectStorage.id,
            fileUri = objectStorage.fileUri
        )

        context("파일 업로드 하면") {
            val request = multipart(url, bucket)
                .file(file)
                .contentType(MULTIPART_FORM_DATA)


            it("200 OK") {
                every { proxy.upload(any(), any(), any()) } returns objectStorage

                mockMvc.perform(request)
                    .andExpect(status().isOk)
                    .andExpectData(
                        jsonPath("$.fileId") shouldBe response.fileId,
                        jsonPath("$.fileUri") shouldBe response.fileUri
                    )
                    .andDocument(
                        "create-object-storage",
                        pathParameters(
                            "bucket" isRequired true pathMeans "저장할 버킷명(PROFILE: 프로필 이미지, FEED: 피드 이미지, CREW: 크루 대표/로고, ACTIVITY: 행사 이미지)"
                        ),
                        // TODO: file 추가
                        responseBody(
                            "fileId" type NUMBER means "식별자" isRequired true,
                            "fileUri" type STRING means "uri" isRequired true
                        )
                    )
            }
        }
    }

    describe("DELETE : $baseUrl") {
        val url = "$baseUrl/{bucket}/{fileId}"
        val bucket = Bucket.FEED.name
        val fileId = 0L

        context("파일 삭제 하면") {
            val request = request(HttpMethod.DELETE, url, bucket, fileId)
                .contentType(APPLICATION_JSON)

            it("200 OK") {
                every { proxy.remove(any(), any(), any()) } just runs

                mockMvc.perform(request)
                    .andExpect(status().isOk)
                    .andDocument(
                        "remove-object-storage",
                        pathParameters(
                            "bucket" isRequired true pathMeans "삭제할 버킷명(PROFILE: 프로필 이미지, FEED: 피드 이미지, CREW: 크루 대표/로고, ACTIVITY: 행사 이미지)",
                            "fileId" isRequired true pathMeans "삭제할 파일 식별자"
                        )
                    )
            }
        }
    }
})
