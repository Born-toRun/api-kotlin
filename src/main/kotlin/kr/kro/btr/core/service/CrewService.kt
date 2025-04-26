package kr.kro.btr.core.service

import kr.kro.btr.core.converter.CrewConverter
import kr.kro.btr.domain.entity.CrewEntity
import kr.kro.btr.domain.port.CrewPort
import kr.kro.btr.domain.port.model.CreateCrewCommand
import kr.kro.btr.domain.port.model.Crew
import kr.kro.btr.infrastructure.CrewGateway
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CrewService(
    private val crewConverter: CrewConverter,
    private val crewGateway: CrewGateway
) : CrewPort {

    @Transactional(readOnly = true)
    override fun searchAll(): List<Crew> {
        val crewEntities: List<CrewEntity> = crewGateway.searchAll()
        return crewConverter.map(crewEntities)
    }

    @Transactional
    override fun create(command: CreateCrewCommand) {
        val query = crewConverter.map(command)
        crewGateway.create(query)
    }
}