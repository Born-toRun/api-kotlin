package kr.kro.btr.domain.port.model

data class CreateCrewCommand(
    val name: String,
    val contents: String,
    val sns: String?,
    val region: String
)