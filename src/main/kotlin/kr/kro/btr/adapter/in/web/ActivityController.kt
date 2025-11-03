package kr.kro.btr.adapter.`in`.web

import jakarta.validation.Valid
import kr.kro.btr.adapter.`in`.web.payload.AttendanceActivityRequest
import kr.kro.btr.adapter.`in`.web.payload.CreateActivityRequest
import kr.kro.btr.adapter.`in`.web.payload.DetailActivityResponse
import kr.kro.btr.adapter.`in`.web.payload.ModifyActivityRequest
import kr.kro.btr.adapter.`in`.web.payload.MyParticipationsResponse
import kr.kro.btr.adapter.`in`.web.payload.OpenActivityResponse
import kr.kro.btr.adapter.`in`.web.payload.ParticipationActivityResponse
import kr.kro.btr.adapter.`in`.web.payload.SearchActivitiesRequest
import kr.kro.btr.adapter.`in`.web.payload.SearchActivitiesResponse
import kr.kro.btr.adapter.`in`.web.proxy.ActivityProxy
import kr.kro.btr.base.extension.toMyParticipationsResponse
import kr.kro.btr.base.extension.toOpenActivityResponse
import kr.kro.btr.base.extension.toParticipationActivityResponse
import kr.kro.btr.base.extension.toSearchActivityDetailResponse
import kr.kro.btr.base.extension.toSearchActivityResponse
import kr.kro.btr.support.TokenDetail
import kr.kro.btr.support.annotation.AuthUser
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/activities")
class ActivityController(
    private val activityProxy: ActivityProxy
) {

    @PostMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun create(@AuthUser my: TokenDetail, @Valid @RequestBody request: CreateActivityRequest): ResponseEntity<Void> {
        activityProxy.create(my, request)
        return ResponseEntity(CREATED)
    }

    @PutMapping("/{activityId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun modify(
        @PathVariable activityId: Long,
        @Valid @RequestBody request: ModifyActivityRequest
    ): ResponseEntity<Void> {
        activityProxy.modify(request, activityId)
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/{activityId}")
    fun remove(@PathVariable activityId: Long): ResponseEntity<Void> {
        activityProxy.remove(activityId)
        return ResponseEntity.ok().build()
    }

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun searchAll(
        @AuthUser my: TokenDetail,
        @Valid @ModelAttribute request: SearchActivitiesRequest
    ): ResponseEntity<SearchActivitiesResponse> {
        val activities = activityProxy.searchAll(request, my)
        val response = activities.toSearchActivityResponse()
        return ResponseEntity.ok(response)
    }

    @GetMapping("/crew/{crewId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun searchByCrewId(
        @PathVariable crewId: Long,
        @Valid @ModelAttribute request: SearchActivitiesRequest
    ): ResponseEntity<SearchActivitiesResponse> {
        val activities = activityProxy.searchByCrewId(crewId, request)
        val response = activities.toSearchActivityResponse()
        return ResponseEntity.ok(response)
    }

    @GetMapping("/{activityId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun search(
        @AuthUser my: TokenDetail,
        @PathVariable activityId: Long
    ): ResponseEntity<DetailActivityResponse> {
        val activity = activityProxy.search(activityId, my)
        val response = activity.toSearchActivityDetailResponse()
        return ResponseEntity.ok(response)
    }

    @PutMapping("/open/{activityId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun open(@PathVariable activityId: Long): ResponseEntity<OpenActivityResponse> {
        val activityResult = activityProxy.open(activityId)
        val response = activityResult.toOpenActivityResponse()
        return ResponseEntity.ok(response)
    }

    @PostMapping("/participation/{activityId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun participate(@AuthUser my: TokenDetail, @PathVariable activityId: Long): ResponseEntity<Void> {
        activityProxy.participate(activityId, my.id)
        return ResponseEntity(CREATED)
    }

    @PostMapping("/participation-cancel/{participationId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun participateCancel(@PathVariable participationId: Long): ResponseEntity<Void> {
        activityProxy.participateCancel(participationId)
        return ResponseEntity(CREATED)
    }

    @GetMapping("/participation/{activityId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun searchAttendance(
        @PathVariable activityId: Long
    ): ResponseEntity<ParticipationActivityResponse> {
        val participationResult = activityProxy.getParticipation(activityId)
        val response = participationResult.toParticipationActivityResponse()
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

    @GetMapping("/participation/my", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun searchMyParticipations(
        @AuthUser my: TokenDetail
    ): ResponseEntity<MyParticipationsResponse> {
        val activities = activityProxy.searchMyParticipations(my.id)
        val response = activities.toMyParticipationsResponse()
        return ResponseEntity.ok(response)
    }
}
