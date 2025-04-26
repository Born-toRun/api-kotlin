package kr.kro.btr.core.service

import kr.kro.btr.core.converter.PrivacyConverter
import kr.kro.btr.domain.port.PrivacyPort
import kr.kro.btr.domain.port.model.ModifyUserPrivacyCommand
import kr.kro.btr.domain.port.model.UserPrivacy
import kr.kro.btr.infrastructure.PrivacyGateway
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PrivacyService(
    private val privacyConverter: PrivacyConverter,
    private val privacyGateway: PrivacyGateway
) : PrivacyPort {

    @Transactional
    override fun modifyUserPrivacy(command: ModifyUserPrivacyCommand) {
        val query = privacyConverter.map(command)
        privacyGateway.modifyUserPrivacy(query)
    }

    @Transactional(readOnly = true)
    override fun searchUserPrivacy(userId: Long): UserPrivacy {
        val userPrivacyEntity = privacyGateway.searchUserPrivacy(userId)
        return privacyConverter.map(userPrivacyEntity)
    }
}