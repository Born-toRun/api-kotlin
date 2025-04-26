package kr.kro.btr.domain.port.model

data class SearchAllMarathonCommand(
    val locations: List<String>,
    val courses: List<String>,
    val myUserId: Long
)
