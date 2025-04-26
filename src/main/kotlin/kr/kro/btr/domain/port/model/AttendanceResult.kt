package kr.kro.btr.domain.port.model

data class AttendanceResult(
    val host: Participant,
    val participants: List<Participant>
) {
    data class Participant(
        val userId: Long,
        val userName: String?,
        val crewName: String?,
        val userProfileUri: String?
    )
}
