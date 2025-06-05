package kr.kro.btr.adapter.`in`.web.payload

data class SearchCrewResponse(
    val details: List<Detail>
) {
    data class Detail(
        val id: Long,
        val crewName: String,
        val contents: String = createDefaultContents(crewName),
        val region: String,
        val imageUri: String?,
        val logoUri: String?,
        val crewSnsUri: String?
    ) {
        companion object {
            private fun createDefaultContents(crewName: String) = "안녕하세요. $crewName 입니다."
        }
    }
}
