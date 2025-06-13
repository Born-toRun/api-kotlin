package kr.kro.btr.adapter.`in`.web.proxy

import kr.kro.btr.adapter.`in`.web.payload.AttendanceActivityRequest
import kr.kro.btr.adapter.`in`.web.payload.CreateActivityRequest
import kr.kro.btr.adapter.`in`.web.payload.ModifyActivityRequest
import kr.kro.btr.adapter.`in`.web.payload.SearchActivitiesRequest
import kr.kro.btr.base.extension.toAttendanceActivityCommand
import kr.kro.btr.base.extension.toCreateActivityCommand
import kr.kro.btr.base.extension.toModifyActivityCommand
import kr.kro.btr.base.extension.toSearchAllActivityCommand
import kr.kro.btr.domain.port.ActivityPort
import kr.kro.btr.domain.port.model.result.ActivityResult
import kr.kro.btr.domain.port.model.result.ParticipantResult
import kr.kro.btr.domain.port.model.ParticipateActivityCommand
import kr.kro.btr.support.TokenDetail
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component

@Component
@CacheConfig(cacheNames = ["activity"])
class ActivityProxy(
    private val activityPort: ActivityPort
) {

    @CacheEvict(allEntries = true)
    fun create(my: TokenDetail, request: CreateActivityRequest) {
        val command = request.toCreateActivityCommand(my)
        activityPort.create(command)
    }

    @CacheEvict(allEntries = true)
    fun modify(request: ModifyActivityRequest, activityId: Long) {
        val command = request.toModifyActivityCommand(activityId)
        activityPort.modify(command)
    }

    @CacheEvict(allEntries = true)
    fun remove(activityId: Long) {
        activityPort.remove(activityId)
    }

    @CacheEvict(allEntries = true)
    fun participate(activityId: Long, myUserId: Long) {
        val command = ParticipateActivityCommand(activityId, myUserId)
        activityPort.participate(command)
    }

    @CacheEvict(allEntries = true)
    fun participateCancel(participationId: Long) {
        activityPort.participateCancel(participationId)
    }

    @Cacheable(key = "'searchAll: ' + #my.id + #request.hashCode()")
    fun searchAll(request: SearchActivitiesRequest, my: TokenDetail): List<ActivityResult> {
        val command = request.toSearchAllActivityCommand(my)
        return activityPort.searchAll(command)
    }

    @Cacheable(key = "'search: ' + #my.id + #activityId")
    fun search(activityId: Long, my: TokenDetail): ActivityResult {
        return activityPort.search(activityId, my.id)
    }

    @CacheEvict(allEntries = true)
    fun open(activityId: Long): ActivityResult {
        return activityPort.open(activityId)
    }

    @CacheEvict(allEntries = true)
    fun attendance(request: AttendanceActivityRequest, activityId: Long, myUserId: Long) {
        val command = request.toAttendanceActivityCommand(activityId, myUserId)
        activityPort.attendance(command)
    }

    @Cacheable(key = "'getParticipation: ' + #activityId")
    fun getParticipation(activityId: Long): ParticipantResult {
        return activityPort.getParticipation(activityId)
    }
}
