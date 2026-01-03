package kr.kro.btr.infrastructure.model

data class CreateCrewQuery(
    val name: String,
    val contents: String,
    val sns: String? = null,
    val region: String,
    val userId: Long
)