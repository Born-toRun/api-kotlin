package kr.kro.btr.core.converter

import kr.kro.btr.adapter.`in`.web.payload.SearchUserPrivacyResponse
import kr.kro.btr.adapter.`in`.web.payload.SettingUserPrivacyRequest
import kr.kro.btr.domain.entity.UserPrivacyEntity
import kr.kro.btr.domain.port.model.ModifyUserPrivacyCommand
import kr.kro.btr.domain.port.model.UserPrivacy
import kr.kro.btr.infrastructure.model.ModifyUserPrivacyQuery
import org.springframework.stereotype.Component

@Component
class PrivacyConverter {

    fun map(source: UserPrivacy): SearchUserPrivacyResponse {
        return SearchUserPrivacyResponse(
            isInstagramIdPublic = source.isInstagramIdPublic
        )
    }

    fun map(source: SettingUserPrivacyRequest, userId: Long): ModifyUserPrivacyCommand {
        return ModifyUserPrivacyCommand(
            myUserId = userId,
            isInstagramIdPublic = source.isInstagramIdPublic
        )
    }

    fun map(source: ModifyUserPrivacyCommand): ModifyUserPrivacyQuery {
        return ModifyUserPrivacyQuery(
            myUserId = source.myUserId,
            isInstagramIdPublic = source.isInstagramIdPublic
        )
    }

    fun map(source: UserPrivacyEntity): UserPrivacy {
        return UserPrivacy(
            id = source.id,
            userId = source.userId,
            isInstagramIdPublic = source.isInstagramIdPublic
        )
    }
}
