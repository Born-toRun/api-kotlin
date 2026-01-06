package kr.kro.btr.adapter.`in`.web.payload

data class CrewMemberRankingResponse(
    val rankings: List<MemberRank>
) {
    data class MemberRank(
        val rank: Int,
        val userId: Long,
        val userName: String,
        val profileImageUri: String?,
        val instagramId: String?,
        val participationCount: Long,
        val isAdmin: Boolean,
        val isManager: Boolean
    )
}
