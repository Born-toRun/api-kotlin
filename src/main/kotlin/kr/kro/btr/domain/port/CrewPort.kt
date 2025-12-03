package kr.kro.btr.domain.port

import kr.kro.btr.domain.port.model.CreateCrewCommand
import kr.kro.btr.domain.port.model.ModifyCrewCommand
import kr.kro.btr.domain.port.model.result.CrewMemberRankingResult
import kr.kro.btr.domain.port.model.result.CrewMemberResult
import kr.kro.btr.domain.port.model.result.CrewRankingResult
import kr.kro.btr.domain.port.model.result.CrewResult

interface CrewPort {
    fun searchAll(): List<CrewResult>
    fun detail(crewId: Long): CrewResult
    fun create(command: CreateCrewCommand)
    fun modify(command: ModifyCrewCommand)
    fun searchMembers(crewId: Long): List<CrewMemberResult>
    fun searchRankings(): List<CrewRankingResult>
    fun searchMemberRankings(crewId: Long): List<CrewMemberRankingResult>
}
