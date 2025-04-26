package kr.kro.btr.infrastructure

import kr.kro.btr.adapter.out.persistence.UserPrivacyRepository
import kr.kro.btr.domain.entity.UserPrivacyEntity
import kr.kro.btr.infrastructure.model.ModifyUserPrivacyQuery
import kr.kro.btr.support.exception.NotFoundException
import org.springframework.stereotype.Component

@Component
class PrivacyGateway(
    private val userPrivacyRepository: UserPrivacyRepository
) {

    fun modifyUserPrivacy(query: ModifyUserPrivacyQuery) {
        val userPrivacy = searchUserPrivacy(query.myUserId)
        userPrivacy.change(query.isInstagramIdPublic)

        userPrivacyRepository.save(userPrivacy)
    }

    fun searchUserPrivacy(userId: Long): UserPrivacyEntity {
        return userPrivacyRepository.findByUserId(userId)
            ?: throw NotFoundException("정보 노출 동의 내용을 찾을 수 없습니다.")
    }
}
