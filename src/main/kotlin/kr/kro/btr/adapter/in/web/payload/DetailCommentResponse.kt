package kr.kro.btr.adapter.`in`.web.payload

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import java.time.LocalDateTime

data class DetailCommentResponse(
    val id: Long,
    val writer: Writer,
    val contents: String,
    @JsonDeserialize(using = LocalDateTimeDeserializer::class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val registeredAt: LocalDateTime,
    val reComments: List<ReComment>
) {
    data class Writer(
        val userId: Long,
        val userName: String,
        val profileImageUri: String?,
        val crewName: String,
        val isAdmin: Boolean,
        val isManager: Boolean
    )

    data class ReComment(
        val id: Long,
        val contents: String,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        val registeredAt: LocalDateTime,
        val writer: Writer,
        val isMyComment: Boolean
    ) {
        data class Writer(
            val userId: Long,
            val userName: String,
            val profileImageUri: String?,
            val crewName: String,
            val isAdmin: Boolean,
            val isManager: Boolean
        )
    }
}
