package kr.kro.btr.adapter.`in`.web.payload

data class MyCrewDetailResponse(
    val id: Long,
    val crewName: String,
    val contents: String = "안녕하세요. $crewName 입니다.",
    val region: String,
    val imageUri: String?,
    val logoUri: String?,
    val crewSnsUri: String?,
    val isManager: Boolean,
    val isAdmin: Boolean
)