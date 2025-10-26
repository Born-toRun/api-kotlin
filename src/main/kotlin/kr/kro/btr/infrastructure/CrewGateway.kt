package kr.kro.btr.infrastructure

import kr.kro.btr.adapter.out.persistence.CrewRepository
import kr.kro.btr.adapter.out.persistence.UserRepository
import kr.kro.btr.base.extension.toCrewEntity
import kr.kro.btr.domain.entity.CrewEntity
import kr.kro.btr.domain.entity.UserEntity
import kr.kro.btr.infrastructure.model.CreateCrewQuery
import kr.kro.btr.support.exception.NotFoundException
import org.springframework.stereotype.Component

@Component
class CrewGateway(
    private val crewRepository: CrewRepository,
    private val userRepository: UserRepository
) {

    // TODO: 크루 많아지면 페이지네이션
    fun searchAll(): List<CrewEntity> {
        return crewRepository.findAll()
    }

    fun create(query: CreateCrewQuery) {
        val crewEntity = query.toCrewEntity()
        crewRepository.save(crewEntity)
    }

    fun searchById(crewId: Long): CrewEntity {
        return crewRepository.findByIdOrNull(crewId) ?: throw NotFoundException("크루를 찾을 수 없습니다.")
    }

    fun searchMembersByCrewId(crewId: Long): List<UserEntity> {
        return userRepository.findAllByCrewId(crewId)
    }
}
