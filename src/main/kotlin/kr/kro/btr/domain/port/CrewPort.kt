package kr.kro.btr.domain.port

import kr.kro.btr.domain.port.model.CreateCrewCommand
import kr.kro.btr.domain.port.model.result.CrewResult

interface CrewPort {
    fun searchAll(): List<CrewResult>
    fun detail(crewId: Long): CrewResult
    fun create(command: CreateCrewCommand)
}
