package kr.kro.btr.core.service

import kr.kro.btr.base.extension.toCreateCrewQuery
import kr.kro.btr.base.extension.toCrew
import kr.kro.btr.base.extension.toCrewMembers
import kr.kro.btr.base.extension.toCrews
import kr.kro.btr.base.extension.toModifyCrewQuery
import kr.kro.btr.domain.entity.CrewEntity
import kr.kro.btr.domain.entity.UserEntity
import kr.kro.btr.domain.port.CrewPort
import kr.kro.btr.domain.port.model.CreateCrewCommand
import kr.kro.btr.domain.port.model.ModifyCrewCommand
import kr.kro.btr.domain.port.model.result.CrewMemberRankingResult
import kr.kro.btr.domain.port.model.result.CrewMemberResult
import kr.kro.btr.domain.port.model.result.CrewRankingResult
import kr.kro.btr.domain.port.model.result.CrewResult
import kr.kro.btr.infrastructure.CrewGateway
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CrewService(
    private val crewGateway: CrewGateway
) : CrewPort {

    @Transactional(readOnly = true)
    override fun searchAll(): List<CrewResult> {
        val crewEntities: List<CrewEntity> = crewGateway.searchAll()
        return crewEntities.toCrews()
    }

    @Transactional(readOnly = true)
    override fun detail(crewId: Long): CrewResult {
        val crewEntity = crewGateway.searchById(crewId)
        return crewEntity.toCrew()
    }

    @Transactional(readOnly = true)
    override fun detailMyCrew(myCrewId: Long): CrewResult {
        val crewEntity = crewGateway.searchById(myCrewId)
        return crewEntity.toCrew()
    }

    @Transactional
    override fun create(command: CreateCrewCommand) {
        val query = command.toCreateCrewQuery()
        crewGateway.create(query)
    }

    @Transactional
    override fun modify(command: ModifyCrewCommand) {
        val query = command.toModifyCrewQuery()
        crewGateway.modify(query)
    }

    @Transactional(readOnly = true)
    override fun searchMembers(crewId: Long): List<CrewMemberResult> {
        val userEntities: List<UserEntity> = crewGateway.searchMembersByCrewId(crewId)
        return userEntities.toCrewMembers()
    }

    @Transactional(readOnly = true)
    override fun searchRankings(): List<CrewRankingResult> {
        return crewGateway.searchRankings()
    }

    @Transactional(readOnly = true)
    override fun searchMemberRankings(crewId: Long): List<CrewMemberRankingResult> {
        return crewGateway.searchMemberRankings(crewId)
    }
}
