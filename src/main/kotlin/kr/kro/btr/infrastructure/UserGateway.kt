package kr.kro.btr.infrastructure

import kr.kro.btr.adapter.out.persistence.UserPrivacyRepository
import kr.kro.btr.adapter.out.persistence.UserRepository
import kr.kro.btr.base.extension.toUserEntity
import kr.kro.btr.domain.entity.UserEntity
import kr.kro.btr.domain.entity.UserPrivacyEntity
import kr.kro.btr.domain.port.model.result.CrewMemberRankingResult
import kr.kro.btr.infrastructure.model.CreateUserQuery
import kr.kro.btr.infrastructure.model.ModifyUserQuery
import kr.kro.btr.infrastructure.model.SignUpUserQuery
import kr.kro.btr.support.exception.NotFoundException
import org.springframework.stereotype.Component

@Component
class UserGateway(
    private val userRepository: UserRepository,
    private val userPrivacyRepository: UserPrivacyRepository,
) {

    fun searchBySocialId(socialId: String): UserEntity {
        return userRepository.findBySocialId(socialId)
            ?: throw NotFoundException("회원을 찾을 수 없습니다.")
    }

    fun searchById(userId: Long): UserEntity {
        return userRepository.findAllEntitiesById(userId)
            ?: throw NotFoundException("회원을 찾을 수 없습니다.")
    }

    fun modify(query: ModifyUserQuery): UserEntity {
        val userEntity = searchById(query.userId)
        userEntity.modify(query.instagramId, query.profileImageId)
        return userRepository.save(userEntity)
    }

    fun signUp(query: SignUpUserQuery): String {
        val userEntity = searchById(query.userId)
        userEntity.modify(query.userName, query.crewId, query.instagramId, query.roleType)
        return userRepository.save(userEntity).name!!
    }

    fun searchByUserName(userName: String): List<UserEntity> {
        return userRepository.findAllByNameContaining(userName)
    }

    fun remove(userId: Long) {
        val userEntity = userRepository.findAllEntitiesById(userId)
            ?: throw NotFoundException("회원을 찾을 수 없습니다.")
        userRepository.delete(userEntity)
    }

    fun createAndFlush(query: CreateUserQuery): UserEntity {
        val userEntity = query.toUserEntity()

        userRepository.save(userEntity)

        val userPrivacyEntity = UserPrivacyEntity(
            isInstagramIdPublic = false,
            userId = userEntity.id
        )

        userEntity.add(userPrivacyEntity)
        userPrivacyRepository.saveAndFlush(userPrivacyEntity)

        return userEntity
    }

    fun exists(socialId: String): Boolean {
        return userRepository.existsBySocialId(socialId)
    }

    fun searchMembersByCrewId(crewId: Long): List<UserEntity> {
        return userRepository.findAllByCrewId(crewId)
    }

    fun searchMemberRankings(crewId: Long): List<CrewMemberRankingResult> {
        return userRepository.findCrewMemberRankings(crewId)
    }

    fun moveCrewByUser(userId: Long, crewId: Long) {
        val user = userRepository.findById(userId)
            .orElseThrow { throw NotFoundException("사용자를 찾을 수 없습니다.") }
        user.crewId = crewId
        userRepository.save(user)
    }
}
