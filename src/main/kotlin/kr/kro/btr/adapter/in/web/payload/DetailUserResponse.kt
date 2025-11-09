package kr.kro.btr.adapter.`in`.web.payload

data class DetailUserResponse(
    val userId: Long,
    val userName: String?,
    val crewName: String?,
    val profileImageUri: String?,
    val isAdmin: Boolean,
    val isManager: Boolean,
    val yellowCardQty: Int,
    val instagramId: String?,
    val isInstagramIdPublic: Boolean?
)
