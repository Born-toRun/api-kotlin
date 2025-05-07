package kr.kro.btr.adapter.`in`.web.payload

import com.fasterxml.jackson.annotation.JsonIgnore

data class UserDetailResponse(
    val userId: Long,
    val userName: String?,
    val crewName: String?,
    val profileImageUri: String?,
    val isAdmin: Boolean,
    val isManager: Boolean,
    val yellowCardQty: Int,
    @JsonIgnore
    val instagramId: String?,
    val isInstagramIdPublic: Boolean?
) {
    val instagramUri: String?
        get() = instagramId?.let { "https://www.instagram.com/$it" }
}
