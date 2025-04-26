package kr.kro.btr.infrastructure.model

data class SearchMarathonQuery(val locations: List<String>,
                               val courses: List<String>)