package kr.kro.btr.domain.port.model.result

data class CrewRankingResult(
    val crewId: Long,
    val crewName: String,
    val contents: String,
    val region: String,
    val imageUri: String?,
    val logoUri: String?,
    val sns: String?,
    val activityCount: Long
)
