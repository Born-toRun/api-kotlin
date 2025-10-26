package kr.kro.btr.domain.port.model.result

data class CrewMemberResult(
    val userId: Long,
    val userName: String,
    val profileImageUri: String?,
    val instagramId: String?,
    val isManager: Boolean,
    val isAdmin: Boolean
)