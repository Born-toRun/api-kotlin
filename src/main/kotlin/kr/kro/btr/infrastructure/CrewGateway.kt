package kr.kro.btr.infrastructure

import kr.kro.btr.adapter.out.persistence.CrewRepository
import kr.kro.btr.core.converter.CrewConverter
import kr.kro.btr.domain.entity.CrewEntity
import kr.kro.btr.infrastructure.model.CreateCrewQuery
import org.springframework.stereotype.Component

@Component
class CrewGateway(
    private val crewConverter: CrewConverter,
    private val crewRepository: CrewRepository
) {

    // TODO: 크루 많아지면 페이지네이션
    fun searchAll(): List<CrewEntity> {
        return crewRepository.findAll()
    }

    fun create(query: CreateCrewQuery) {
        val crewEntity = crewConverter.map(query)
        crewRepository.save(crewEntity)
    }
}
