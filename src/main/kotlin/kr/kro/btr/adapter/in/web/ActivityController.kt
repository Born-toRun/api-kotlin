package kr.kro.btr.adapter.`in`.web

import jakarta.validation.Valid
import kr.kro.btr.adapter.`in`.web.payload.AttendanceActivityRequest
import kr.kro.btr.adapter.`in`.web.payload.AttendanceActivityResponse
import kr.kro.btr.adapter.`in`.web.payload.CreateActivityRequest
import kr.kro.btr.adapter.`in`.web.payload.ModifyActivityRequest
import kr.kro.btr.adapter.`in`.web.payload.OpenActivityResponse
import kr.kro.btr.adapter.`in`.web.payload.SearchActivityDetailResponse
import kr.kro.btr.adapter.`in`.web.payload.SearchActivityResponse
import kr.kro.btr.adapter.`in`.web.payload.SearchAllActivityRequest
import kr.kro.btr.adapter.`in`.web.proxy.ActivityProxy
import kr.kro.btr.core.converter.ActivityConverter
import kr.kro.btr.support.TokenDetail
import kr.kro.btr.support.annotation.AuthUser
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/activities")
class ActivityController(
    private val activityConverter: ActivityConverter,
    private val activityProxy: ActivityProxy
) {

    @PostMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun create(@AuthUser my: TokenDetail, @Valid @RequestBody request: CreateActivityRequest): ResponseEntity<Void> {
        activityProxy.create(my, request)
        return ResponseEntity(CREATED)
    }

    @PutMapping("/{activityId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun modify(
        @AuthUser my: TokenDetail,
        @PathVariable activityId: Long,
        @Valid @RequestBody request: ModifyActivityRequest
    ): ResponseEntity<Void> {
        activityProxy.modify(request, activityId)
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/{activityId}")
    fun remove(@AuthUser my: TokenDetail, @PathVariable activityId: Long): ResponseEntity<Void> {
        activityProxy.remove(activityId)
        return ResponseEntity.ok().build()
    }

    @PostMapping("/participation/{activityId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun participate(@AuthUser my: TokenDetail, @PathVariable activityId: Long): ResponseEntity<Void> {
        activityProxy.participate(activityId, my.id)
        return ResponseEntity(CREATED)
    }

    @PostMapping("/participation-cancel/{participationId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun participateCancel(@AuthUser my: TokenDetail, @PathVariable participationId: Long): ResponseEntity<Void> {
        activityProxy.participateCancel(participationId)
        return ResponseEntity(CREATED)
    }

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun searchAll(
        @AuthUser my: TokenDetail,
        @Valid @ModelAttribute request: SearchAllActivityRequest
    ): ResponseEntity<SearchActivityResponse> {
        val activities = activityProxy.searchAll(request, my)
        val response = activityConverter.map(activities)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/{activityId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun search(
        @AuthUser my: TokenDetail,
        @PathVariable activityId: Long
    ): ResponseEntity<SearchActivityDetailResponse> {
        val activity = activityProxy.search(activityId, my)
        val response = activityConverter.mapToSearchActivityDetailResponse(activity)
        return ResponseEntity.ok(response)
    }

    @PutMapping("/open/{activityId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun open(@AuthUser my: TokenDetail, @PathVariable activityId: Long): ResponseEntity<OpenActivityResponse> {
        val activity = activityProxy.open(activityId)
        val response = activityConverter.mapToOpenActivityResponse(activity)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/attendance/{activityId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun searchAttendance(
        @AuthUser my: TokenDetail,
        @PathVariable activityId: Long
    ): ResponseEntity<AttendanceActivityResponse> {
        val attendanceResult = activityProxy.getAttendance(activityId)
        val response = activityConverter.map(attendanceResult)
        return ResponseEntity.ok(response)
    }

    @PostMapping("/attendance/{activityId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun attendance(
        @AuthUser my: TokenDetail,
        @PathVariable activityId: Long,
        @Valid @RequestBody request: AttendanceActivityRequest
    ): ResponseEntity<Void> {
        activityProxy.attendance(request, activityId, my.id)
        return ResponseEntity.ok().build()
    }
}
