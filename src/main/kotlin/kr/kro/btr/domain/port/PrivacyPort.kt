package kr.kro.btr.domain.port

import kr.kro.btr.domain.port.model.ModifyUserPrivacyCommand
import kr.kro.btr.domain.port.model.result.UserPrivacyResult

interface PrivacyPort {
    fun modifyUserPrivacy(command: ModifyUserPrivacyCommand)
    fun searchUserPrivacy(userId: Long): UserPrivacyResult
}
