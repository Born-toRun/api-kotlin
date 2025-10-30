package kr.kro.btr.domain.model

data class ModifyCrewQuery(
    val crewId: Long,
    val name: String,
    val contents: String,
    val sns: String?,
    val region: String,
    val imageId: Long?,
    val logoId: Long?
)
