package kr.kro.btr.adapter.`in`.web.payload

data class AttendanceActivityResponse(
    val host: Person,
    // TODO: nullable
    val participants: List<Person>
) {
    data class Person(
        val userId: Long,
        val userName: String?,
        val crewName: String?,
        val userProfileUri: String?
    )
}

