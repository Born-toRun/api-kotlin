package kr.kro.btr.core.service

import kr.kro.btr.base.extension.*
import kr.kro.btr.domain.constant.RoleType
import kr.kro.btr.domain.entity.CrewEntity
import kr.kro.btr.domain.entity.UserEntity
import kr.kro.btr.domain.port.CrewPort
import kr.kro.btr.domain.port.model.CreateCrewCommand
import kr.kro.btr.domain.port.model.KickCrewMemberCommand
import kr.kro.btr.domain.port.model.ModifyCrewCommand
import kr.kro.btr.domain.port.model.result.CrewMemberRankingResult
import kr.kro.btr.domain.port.model.result.CrewMemberResult
import kr.kro.btr.domain.port.model.result.CrewRankingResult
import kr.kro.btr.domain.port.model.result.CrewResult
import kr.kro.btr.infrastructure.CrewGateway
import kr.kro.btr.infrastructure.UserGateway
import kr.kro.btr.support.exception.ForBiddenException
import kr.kro.btr.support.exception.InvalidException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CrewService(
    private val crewGateway: CrewGateway,
    private val userGateway: UserGateway
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

    @Transactional
    override fun create(command: CreateCrewCommand): CrewResult {
        val query = command.toCreateCrewQuery()
        val crewEntity = crewGateway.create(query)
        return crewEntity.toCrew()
    }

    @Transactional
    override fun modify(command: ModifyCrewCommand) {
        val query = command.toModifyCrewQuery()
        crewGateway.modify(query)
    }

    @Transactional(readOnly = true)
    override fun searchMembers(crewId: Long): List<CrewMemberResult> {
        val userEntities: List<UserEntity> = userGateway.searchMembersByCrewId(crewId)
        return userEntities.toCrewMembers()
    }

    @Transactional(readOnly = true)
    override fun searchRankings(): List<CrewRankingResult> {
        return crewGateway.searchRankings()
    }

    @Transactional(readOnly = true)
    override fun searchMemberRankings(crewId: Long): List<CrewMemberRankingResult> {
        return userGateway.searchMemberRankings(crewId)
    }

    @Transactional
    override fun kickMember(command: KickCrewMemberCommand) {
        if (command.requesterId == command.targetUserId) {
            throw InvalidException("자기 자신을 강퇴할 수 없습니다.")
        }

        crewGateway.searchById(command.crewId)

        val targetUser = userGateway.searchById(command.targetUserId)

        if (targetUser.crewId != command.crewId) {
            throw InvalidException("해당 사용자는 이 크루의 멤버가 아닙니다.")
        }

        if (targetUser.roleType == RoleType.ADMIN) {
            throw InvalidException("관리자는 강퇴할 수 없습니다.")
        }

        val requester = userGateway.searchById(command.requesterId)
        val isManager = requester.managedCrewId == command.crewId

        if (!command.isRequesterAdmin && !isManager) {
            throw ForBiddenException("크루원을 강퇴할 권한이 없습니다.")
        }

        val defaultCrewName = "무소속"
        val crew = crewGateway.searchByName(defaultCrewName);

        userGateway.moveCrewByUser(command.targetUserId, crew.id)
    }
}
