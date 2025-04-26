package kr.kro.btr.adapter.`in`.web.payload

data class SearchAllMarathonResponse(
    val marathons: List<Marathon>
) {
    data class Marathon(
        val id: Long,
        val title: String?,
        val schedule: String?,
        val venue: String?,
        val course: String?,
        val isBookmarking: Boolean? = false
    )
}
