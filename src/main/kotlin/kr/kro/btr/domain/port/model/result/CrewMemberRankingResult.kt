package kr.kro.btr.domain.port.model.result

data class CrewMemberRankingResult(
    val userId: Long,
    val userName: String,
    val profileImageUri: String?,
    val instagramId: String?,
    val participationCount: Long,
    val isAdmin: Boolean,
    val isManager: Boolean
)
