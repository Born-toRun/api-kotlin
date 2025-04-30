package kr.kro.btr.common.base

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.spec.style.DescribeSpec
import kr.kro.btr.config.WebMvcConfig
import kr.kro.btr.support.Notification
import kr.kro.btr.utils.restdocs.RestDocsField
import kr.kro.btr.utils.restdocs.STRING
import kr.kro.btr.utils.restdocs.responseBody
import kr.kro.btr.utils.restdocs.type
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.springframework.context.annotation.Import
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.payload.ResponseFieldsSnippet

@Import(value = [WebMvcConfig::class])
@ExtendWith(RestDocumentationExtension::class)
abstract class ControllerDescribeSpec(
    body: DescribeSpec.() -> Unit = {}
) : DescribeSpec(body) {

    @MockkBean
    protected lateinit var notification: Notification

    companion object {
        private val mapper: ObjectMapper = jacksonObjectMapper()
            .registerKotlinModule()
            .registerModule(JavaTimeModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)

        fun toJson(value: Any): String {
            return mapper.writeValueAsString(value)
        }

        fun <T> any(type: Class<T>): T = Mockito.any(type)

        fun failedResponseBody(vararg fields: RestDocsField, dataOptional: Boolean = false): ResponseFieldsSnippet {
            return responseBody(
                "message" type STRING means "실패 메시지",
            ).and(fields.map { it.descriptor })
        }
    }
}
