package kr.kro.btr.adapter.`in`.web.payload

data class CreateCrewResponse(
    val id: Long,
    val crewName: String,
    val contents: String,
    val region: String,
    val crewSnsUri: String?,
    val imageUri: String?,
    val logoUri: String?
)
