package kr.kro.btr.domain.port.model.result

import kr.kro.btr.domain.constant.ProviderType
import kr.kro.btr.domain.constant.RoleType
import java.time.LocalDateTime

data class BornToRunUser(
    val userId: Long,
    val socialId: String,
    val providerType: ProviderType,
//    val refreshToken: String?,
    val roleType: RoleType,
    val userName: String?,
    val crewId: Long?,
    val crewName: String?,
    val instagramId: String? = null,
    val imageId: Long? = null,
    val profileImageUri: String?,
    val lastLoginAt: LocalDateTime,
    val isAdmin: Boolean = false,
    val isManager: Boolean = false,
    val yellowCardQty: Int = 0,
    val isInstagramIdPublic: Boolean? = false
)
