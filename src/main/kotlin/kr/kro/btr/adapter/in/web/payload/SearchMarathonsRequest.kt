package kr.kro.btr.adapter.`in`.web.payload

data class SearchMarathonsRequest(
    val locations: List<String>?,
    val courses: List<String>?
)
