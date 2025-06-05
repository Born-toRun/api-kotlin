package kr.kro.btr.domain.port.model.result

data class ParticipantResult(
    val host: Participant,
    val participants: List<Participant>? = null
) {
    data class Participant(
        val participationId: Long? = null,
        val userId: Long,
        val userName: String,
        val crewName: String,
        val userProfileUri: String?
    )
}
