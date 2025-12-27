package kr.kro.btr.core.service

import kr.kro.btr.base.extension.*
import kr.kro.btr.domain.port.ActivityPort
import kr.kro.btr.domain.port.model.*
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
    override fun create(command: CreateActivityCommand): Long {
        val query = command.toCreateActivityQuery()
        return activityGateway.create(query)
    }

    @Transactional
    override fun modify(command: ModifyActivityCommand) {
        val query = command.toModifyActivityQuery()
        activityGateway.modify(query)
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
        val activityAggregations = activityGateway.searchAllWithAggregation(query, command.myUserId)

        return activityAggregations.map { it.toActivityResult(command.myUserId) }
    }

    @Transactional(readOnly = true)
    override fun searchByCrewId(command: SearchByCrewIdActivityCommand): List<ActivityResult> {
        val query = command.toSearchByCrewIdActivityQuery()

        val activityAggregations = activityGateway.searchAllWithAggregation(query, 0L)

        return activityAggregations.map { it.toActivityResult(0L) }
    }

    @Transactional(readOnly = true)
    override fun search(activityId: Long, myUserId: Long): ActivityResult {
        val activityAggregation = activityGateway.searchWithAggregation(activityId, myUserId)

        return activityAggregation.toActivityResult(myUserId)
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

    @Transactional(readOnly = true)
    override fun searchMyParticipations(myUserId: Long): List<ActivityResult> {
        val activityEntities = activityGateway.searchMyParticipations(myUserId)

        if (activityEntities.isEmpty()) return emptyList()

        val activityIds = activityEntities.map { it.id }

        val participationCountMap = activityGateway.getParticipationCountMap(activityIds)

        return activityEntities.map { activity ->
            kr.kro.btr.infrastructure.model.ActivityAggregationData(
                activityEntity = activity,
                participantsCount = participationCountMap[activity.id] ?: 0,
                hasUserParticipation = true
            ).toActivityResult(myUserId)
        }
    }
}
