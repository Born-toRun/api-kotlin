package kr.kro.btr.adapter.`in`.web.payload

data class SearchAllMarathonRequest(
    val locations: List<String>?,
    val courses: List<String>?
)
