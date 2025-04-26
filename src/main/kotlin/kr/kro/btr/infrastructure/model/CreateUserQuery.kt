package kr.kro.btr.infrastructure.model

import kr.kro.btr.domain.constant.ProviderType
import kr.kro.btr.domain.constant.RoleType

data class CreateUserQuery(
    val socialId: String,
    val providerType: ProviderType,
    val roleType: RoleType
)
