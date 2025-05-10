package kr.kro.btr.core.service

import kr.kro.btr.core.converter.ActivityConverter
import kr.kro.btr.domain.port.ActivityPort
import kr.kro.btr.domain.port.model.ActivityResult
import kr.kro.btr.domain.port.model.AttendanceActivityCommand
import kr.kro.btr.domain.port.model.CreateActivityCommand
import kr.kro.btr.domain.port.model.ModifyActivityCommand
import kr.kro.btr.domain.port.model.ParticipantResult
import kr.kro.btr.domain.port.model.ParticipateActivityCommand
import kr.kro.btr.domain.port.model.SearchAllActivityCommand
import kr.kro.btr.infrastructure.ActivityGateway
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ActivityService(
    private val activityConverter: ActivityConverter,
    private val activityGateway: ActivityGateway
) : ActivityPort {

    @Transactional
    override fun create(command: CreateActivityCommand) {
        val query = activityConverter.map(command)
        activityGateway.create(query)
    }

    @Transactional
    override fun modify(command: ModifyActivityCommand) {
        val query = activityConverter.map(command)
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
        val query = activityConverter.map(command)
        activityGateway.participate(query)
    }

    @Transactional
    override fun participateCancel(participationId: Long) {
        activityGateway.participateCancel(participationId)
    }

    @Transactional(readOnly = true)
    override fun searchAll(command: SearchAllActivityCommand): List<ActivityResult> {
        val query = activityConverter.map(command)
        val activityEntities = activityGateway.searchAll(query)
        return activityConverter.map(activityEntities, command.myUserId)
    }

    @Transactional(readOnly = true)
    override fun search(activityId: Long, myUserId: Long): ActivityResult {
        val activityEntity = activityGateway.search(activityId)
        return activityConverter.map(activityEntity, myUserId)
    }

    @Transactional
    override fun open(activityId: Long): ActivityResult {
        val opened = activityGateway.open(activityId)
        val accessCode = activityGateway.initAccessCode(activityId)
        return activityConverter.map(opened, accessCode)
    }

    @Transactional
    override fun attendance(command: AttendanceActivityCommand) {
        val query = activityConverter.map(command)
        activityGateway.attendance(query)
    }

    @Transactional(readOnly = true)
    override fun getParticipation(activityId: Long): ParticipantResult {
        val activityParticipationEntities = activityGateway.searchParticipation(activityId)
        if (activityParticipationEntities.isEmpty()) {
            val activityEntity = activityGateway.search(activityId)
            return activityConverter.mapToParticipantResult(activityEntity)
        }
        return activityConverter.map(activityParticipationEntities)
    }
}
