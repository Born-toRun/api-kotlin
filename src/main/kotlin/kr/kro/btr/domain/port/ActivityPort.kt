package kr.kro.btr.domain.port

import kr.kro.btr.domain.port.model.*
import kr.kro.btr.domain.port.model.result.ActivityResult
import kr.kro.btr.domain.port.model.result.ParticipantResult

interface ActivityPort {
    fun create(command: CreateActivityCommand): Long
    fun modify(command: ModifyActivityCommand)
    fun remove(activityId: Long)
    fun participate(command: ParticipateActivityCommand)
    fun participateCancel(participationId: Long)
    fun searchAll(command: SearchAllActivityCommand): List<ActivityResult>
    fun searchByCrewId(command: SearchByCrewIdActivityCommand): List<ActivityResult>
    fun search(activityId: Long, myUserId: Long): ActivityResult
    fun open(activityId: Long, myUserId: Long): ActivityResult
    fun attendance(command: AttendanceActivityCommand)
    fun getParticipation(activityId: Long): ParticipantResult
    fun searchMyParticipations(myUserId: Long): List<ActivityResult>
}
