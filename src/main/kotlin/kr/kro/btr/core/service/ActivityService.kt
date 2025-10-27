package kr.kro.btr.core.service

import kr.kro.btr.base.extension.toActivityResult
import kr.kro.btr.base.extension.toActivityResults
import kr.kro.btr.base.extension.toAttendanceActivityQuery
import kr.kro.btr.base.extension.toCreateActivityQuery
import kr.kro.btr.base.extension.toModifyActivityQuery
import kr.kro.btr.base.extension.toParticipantResult
import kr.kro.btr.base.extension.toParticipateActivityQuery
import kr.kro.btr.base.extension.toSearchAllActivityQuery
import kr.kro.btr.base.extension.toSearchByCrewIdActivityQuery
import kr.kro.btr.domain.port.ActivityPort
import kr.kro.btr.domain.port.model.AttendanceActivityCommand
import kr.kro.btr.domain.port.model.CreateActivityCommand
import kr.kro.btr.domain.port.model.ModifyActivityCommand
import kr.kro.btr.domain.port.model.ParticipateActivityCommand
import kr.kro.btr.domain.port.model.SearchAllActivityCommand
import kr.kro.btr.domain.port.model.SearchByCrewIdActivityCommand
import kr.kro.btr.domain.port.model.result.ActivityResult
import kr.kro.btr.domain.port.model.result.ParticipantResult
import kr.kro.btr.infrastructure.ActivityGateway
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ActivityService(
    private val activityGateway: ActivityGateway
) : ActivityPort {

    @Transactional
    override fun create(command: CreateActivityCommand) {
        val query = command.toCreateActivityQuery()
        activityGateway.create(query)
    }

    @Transactional
    override fun modify(command: ModifyActivityCommand) {
        val query = command.toModifyActivityQuery()
        activityGateway.modify(query)
    }

    @Transactional
    override fun removeAll(userId: Long) {
        activityGateway.removeAll(userId)
    }

    @Transactional
    override fun remove(activityId: Long) {
        activityGateway.remove(activityId)
    }

    @Transactional
    override fun participate(command: ParticipateActivityCommand) {
        val query = command.toParticipateActivityQuery()
        activityGateway.participate(query)
    }

    @Transactional
    override fun participateCancel(participationId: Long) {
        activityGateway.participateCancel(participationId)
    }

    @Transactional(readOnly = true)
    override fun searchAll(command: SearchAllActivityCommand): List<ActivityResult> {
        val query = command.toSearchAllActivityQuery()
        val activityEntities = activityGateway.searchAll(query)
        return activityEntities.toActivityResults(command.myUserId)
    }

    @Transactional(readOnly = true)
    override fun searchByCrewId(command: SearchByCrewIdActivityCommand): List<ActivityResult> {
        val query = command.toSearchByCrewIdActivityQuery()
        val activityEntities = activityGateway.searchAll(query)
        // Use a default userId of 0 for public access since no user context is available
        return activityEntities.toActivityResults(0L)
    }

    @Transactional(readOnly = true)
    override fun search(activityId: Long, myUserId: Long): ActivityResult {
        val activityEntity = activityGateway.search(activityId)
        return activityEntity.toActivityResult(myUserId)
    }

    @Transactional
    override fun open(activityId: Long): ActivityResult {
        val opened = activityGateway.open(activityId)
        val accessCode = activityGateway.initAccessCode(activityId)
        return opened.toActivityResult(accessCode)
    }

    @Transactional
    override fun attendance(command: AttendanceActivityCommand) {
        val query = command.toAttendanceActivityQuery()
        activityGateway.attendance(query)
    }

    @Transactional(readOnly = true)
    override fun getParticipation(activityId: Long): ParticipantResult {
        val activityParticipationEntities = activityGateway.searchParticipation(activityId)
        if (activityParticipationEntities.isEmpty()) {
            val activityEntity = activityGateway.search(activityId)
            return activityEntity.toParticipantResult()
        }
        return activityParticipationEntities.toParticipantResult()
    }
}
