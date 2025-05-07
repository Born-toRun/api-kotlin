package kr.kro.btr.adapter.`in`.web.payload

data class SearchCrewResponse(
    val crewDetails: List<CrewDetail>
) {
    data class CrewDetail(
        val id: Long,
        val crewName: String,
        val contents: String = "안녕하세요. $crewName 입니다.",
        val region: String,
        val imageUri: String?,
        val logoUri: String?,
        val crewSnsUri: String?
    )
}
