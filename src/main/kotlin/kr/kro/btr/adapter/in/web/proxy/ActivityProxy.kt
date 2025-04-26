package kr.kro.btr.adapter.`in`.web.proxy

import kr.kro.btr.adapter.`in`.web.payload.AttendanceActivityRequest
import kr.kro.btr.adapter.`in`.web.payload.CreateActivityRequest
import kr.kro.btr.adapter.`in`.web.payload.ModifyActivityRequest
import kr.kro.btr.adapter.`in`.web.payload.SearchAllActivityRequest
import kr.kro.btr.core.converter.ActivityConverter
import kr.kro.btr.domain.port.ActivityPort
import kr.kro.btr.domain.port.model.ActivityResult
import kr.kro.btr.domain.port.model.AttendanceResult
import kr.kro.btr.domain.port.model.ParticipateActivityCommand
import kr.kro.btr.support.TokenDetail
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component

@Component
@CacheConfig(cacheNames = ["activity"])
class ActivityProxy(
    private val activityConverter: ActivityConverter,
    private val activityPort: ActivityPort
) {

    @CacheEvict(allEntries = true)
    fun create(my: TokenDetail, request: CreateActivityRequest) {
        val command = activityConverter.map(request, my)
        activityPort.create(command)
    }

    @CacheEvict(allEntries = true)
    fun modify(request: ModifyActivityRequest, activityId: Long) {
        val command = activityConverter.map(request, activityId)
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
    fun searchAll(request: SearchAllActivityRequest, my: TokenDetail): List<ActivityResult> {
        val command = activityConverter.map(request, my)
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
        val command = activityConverter.map(request, activityId, myUserId)
        activityPort.attendance(command)
    }

    @Cacheable(key = "'getAttendance: ' + #activityId")
    fun getAttendance(activityId: Long): AttendanceResult {
        return activityPort.getAttendance(activityId)
    }
}
