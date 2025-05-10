package kr.kro.btr.domain.port

import kr.kro.btr.domain.port.model.ActivityResult
import kr.kro.btr.domain.port.model.AttendanceActivityCommand
import kr.kro.btr.domain.port.model.CreateActivityCommand
import kr.kro.btr.domain.port.model.ModifyActivityCommand
import kr.kro.btr.domain.port.model.ParticipantResult
import kr.kro.btr.domain.port.model.ParticipateActivityCommand
import kr.kro.btr.domain.port.model.SearchAllActivityCommand

interface ActivityPort {
    fun create(command: CreateActivityCommand)
    fun modify(command: ModifyActivityCommand)
    fun removeAll(userId: Long)
    fun remove(activityId: Long)
    fun participate(command: ParticipateActivityCommand)
    fun participateCancel(participationId: Long)
    fun searchAll(command: SearchAllActivityCommand): List<ActivityResult>
    fun search(activityId: Long, myUserId: Long): ActivityResult
    fun open(activityId: Long): ActivityResult
    fun attendance(command: AttendanceActivityCommand)
    fun getParticipation(activityId: Long): ParticipantResult
}
