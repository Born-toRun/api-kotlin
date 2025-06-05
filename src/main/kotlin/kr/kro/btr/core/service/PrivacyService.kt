package kr.kro.btr.core.service

import kr.kro.btr.base.extension.toModifyUserPrivacyQuery
import kr.kro.btr.base.extension.toUserPrivacy
import kr.kro.btr.domain.port.PrivacyPort
import kr.kro.btr.domain.port.model.ModifyUserPrivacyCommand
import kr.kro.btr.domain.port.model.result.UserPrivacyResult
import kr.kro.btr.infrastructure.PrivacyGateway
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PrivacyService(
    private val privacyGateway: PrivacyGateway
) : PrivacyPort {

    @Transactional
    override fun modifyUserPrivacy(command: ModifyUserPrivacyCommand) {
        val query = command.toModifyUserPrivacyQuery()
        privacyGateway.modifyUserPrivacy(query)
    }

    @Transactional(readOnly = true)
    override fun searchUserPrivacy(userId: Long): UserPrivacyResult {
        val userPrivacyEntity = privacyGateway.searchUserPrivacy(userId)
        return userPrivacyEntity.toUserPrivacy()
    }
}
