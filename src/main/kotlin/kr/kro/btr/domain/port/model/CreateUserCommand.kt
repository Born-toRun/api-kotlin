package kr.kro.btr.domain.port.model

import kr.kro.btr.domain.constant.ProviderType
import kr.kro.btr.domain.constant.RoleType

data class CreateUserCommand(
    val socialId: String,
    val providerType: ProviderType,
    val roleType: RoleType
)