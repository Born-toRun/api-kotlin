package kr.kro.btr.domain.port.model

data class Marathon(
    val id: Long,
    val title: String?,
    val schedule: String?,
    val venue: String?,
    val course: String?,
    val isBookmarking: Boolean? = false
)