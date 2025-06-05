package kr.kro.btr.adapter.`in`.web.proxy

import kr.kro.btr.adapter.`in`.web.payload.SettingUserPrivacyRequest
import kr.kro.btr.base.extension.toModifyUserPrivacyCommand
import kr.kro.btr.domain.port.PrivacyPort
import kr.kro.btr.domain.port.model.UserPrivacy
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component

@Component
@CacheConfig(cacheNames = ["privacy"])
class PrivacyProxy(
    private val privacyPort: PrivacyPort
) {

    @CacheEvict(allEntries = true)
    fun modifyUserPrivacy(request: SettingUserPrivacyRequest, myUserId: Long) {
        val command = request.toModifyUserPrivacyCommand(myUserId)
        privacyPort.modifyUserPrivacy(command)
    }

    @Cacheable(key = "'searchUserPrivacy: ' + #userId")
    fun searchUserPrivacy(userId: Long): UserPrivacy {
        return privacyPort.searchUserPrivacy(userId)
    }
}
