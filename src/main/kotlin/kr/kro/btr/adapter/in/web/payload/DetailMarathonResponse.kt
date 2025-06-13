package kr.kro.btr.adapter.`in`.web.payload

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class DetailMarathonResponse(val id: Long,
                                  val title: String?,
                                  val owner: String?,
                                  val email: String?,
                                  val schedule: String?,
                                  val contact: String?,
                                  val course: String?,
                                  val location: String?,
                                  val venue: String?,
                                  val host: String?,
                                  val duration: String?,
                                  val homepage: String?,
                                  val venueDetail: String?,
                                  val remark: String?,
                                  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
                                        val registeredAt: LocalDateTime,
                                  val isBookmarking: Boolean? = false
)
