package kr.kro.btr.domain.port

import kr.kro.btr.domain.port.model.CreateCrewCommand
import kr.kro.btr.domain.port.model.Crew

interface CrewPort {
    fun searchAll(): List<Crew>
    fun detail(crewId: Long): Crew
    fun create(command: CreateCrewCommand)
}
