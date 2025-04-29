package kr.kro.btr.adapter.`in`.web.payload

import com.fasterxml.jackson.annotation.JsonInclude

data class SearchCrewResponse(
    val crewDetails: List<CrewDetail>
) {
    @JsonInclude(JsonInclude.Include.NON_NULL)
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
