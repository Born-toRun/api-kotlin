package kr.kro.btr.adapter.`in`.web.payload

data class CrewRankingResponse(
    val rankings: List<CrewRank>
) {
    data class CrewRank(
        val rank: Int,
        val id: Long,
        val crewName: String,
        val contents: String,
        val region: String,
        val imageUri: String?,
        val logoUri: String?,
        val crewSnsUri: String?,
        val activityCount: Long
    )
}
