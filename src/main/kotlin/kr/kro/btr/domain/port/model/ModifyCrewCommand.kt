package kr.kro.btr.domain.port.model

data class ModifyCrewCommand(
    val crewId: Long,
    val name: String,
    val contents: String,
    val sns: String?,
    val region: String,
    val imageId: Long?,
    val logoId: Long?
)