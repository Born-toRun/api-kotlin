package kr.kro.btr.adapter.`in`.web.payload

data class SearchCrewMembersResponse(
    val members: List<Member>
) {
    data class Member(
        val userId: Long,
        val userName: String,
        val profileImageUri: String?,
        val instagramId: String?,
        val isManager: Boolean,
        val isAdmin: Boolean
    )
}