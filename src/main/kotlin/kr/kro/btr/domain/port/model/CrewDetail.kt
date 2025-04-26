package kr.kro.btr.domain.port.model

data class CrewDetail(
    val id: String,
    val crewName: String,
    val contents: String,
    val imageUri: String,
    val logoUri: String,
    val crewSnsUri: String
)