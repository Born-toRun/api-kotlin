package kr.kro.btr.domain.port.model.result

data class CrewResult(
    val id: Long,
    val name: String,
    val contents: String,
    val region: String,
    val sns: String? = null,
    val imageUri: String? = null,
    val logoUri: String? = null
)
