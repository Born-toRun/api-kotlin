package kr.kro.btr.adapter.`in`.web.payload

data class ParticipationActivityResponse(
    val host: Person,
    // TODO: nullable
    val participants: List<Person>? = null
) {
    data class Person(
        val participationId: Long? = null,
        val userId: Long,
        val userName: String,
        val crewName: String,
        val userProfileUri: String?
    )
}

