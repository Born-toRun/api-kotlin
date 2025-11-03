package kr.kro.btr.adapter.`in`.web

import jakarta.validation.Valid
import kr.kro.btr.adapter.`in`.web.payload.CreateCrewRequest
import kr.kro.btr.adapter.`in`.web.payload.CrewMemberRankingResponse
import kr.kro.btr.adapter.`in`.web.payload.CrewRankingResponse
import kr.kro.btr.adapter.`in`.web.payload.DetailCrewResponse
import kr.kro.btr.adapter.`in`.web.payload.ModifyCrewRequest
import kr.kro.btr.adapter.`in`.web.payload.MyCrewDetailResponse
import kr.kro.btr.adapter.`in`.web.payload.SearchCrewMembersResponse
import kr.kro.btr.adapter.`in`.web.payload.SearchCrewsResponse
import kr.kro.btr.adapter.`in`.web.proxy.CrewProxy
import kr.kro.btr.base.extension.toCrewMemberRankingResponse
import kr.kro.btr.base.extension.toCrewRankingResponse
import kr.kro.btr.base.extension.toDetailCrewResponse
import kr.kro.btr.base.extension.toMyCrewDetailResponse
import kr.kro.btr.base.extension.toSearchCrewMembersResponse
import kr.kro.btr.base.extension.toSearchCrewResponse
import kr.kro.btr.domain.port.model.result.CrewMemberRankingResult
import kr.kro.btr.domain.port.model.result.CrewMemberResult
import kr.kro.btr.domain.port.model.result.CrewRankingResult
import kr.kro.btr.domain.port.model.result.CrewResult
import kr.kro.btr.support.TokenDetail
import kr.kro.btr.support.annotation.AuthUser
import kr.kro.btr.support.exception.AuthorizationException
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.lang.Void

@RestController
@RequestMapping("/api/v1/crews")
class CrewController(
    private val crewProxy: CrewProxy
) {

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun searchAll(): ResponseEntity<SearchCrewsResponse> {
        val crewResults: List<CrewResult> = crewProxy.searchAll()
        val response = crewResults.toSearchCrewResponse()
        return ResponseEntity.ok(response)
    }

    @GetMapping("/{crewId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun detail(@PathVariable crewId: Long): ResponseEntity<DetailCrewResponse> {
        val crew = crewProxy.detail(crewId)
        val response = crew.toDetailCrewResponse()
        return ResponseEntity.ok(response)
    }

    @GetMapping("/my", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun detailMyCrew(@AuthUser my: TokenDetail): ResponseEntity<MyCrewDetailResponse> {
        val crew = crewProxy.detailMyCrew(my.crewId)
        val response = crew.toMyCrewDetailResponse(my.isManager)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/{crewId}/members", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun searchMembers(@PathVariable crewId: Long): ResponseEntity<SearchCrewMembersResponse> {
        val members: List<CrewMemberResult> = crewProxy.searchMembers(crewId)
        val response = members.toSearchCrewMembersResponse()
        return ResponseEntity.ok(response)
    }

    @PostMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun create(@RequestBody @Valid request: CreateCrewRequest): ResponseEntity<Void> {
        crewProxy.create(request)
        return ResponseEntity(CREATED)
    }

    @PutMapping("/{crewId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun modify(
        @PathVariable crewId: Long,
        @Valid @RequestBody request: ModifyCrewRequest
    ): ResponseEntity<Void> {
        crewProxy.modify(request, crewId)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/rankings", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun searchRankings(): ResponseEntity<CrewRankingResponse> {
        val rankings: List<CrewRankingResult> = crewProxy.searchRankings()
        val response = rankings.toCrewRankingResponse()
        return ResponseEntity.ok(response)
    }

    @GetMapping("/member-rankings", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun searchMemberRankings(@AuthUser my: TokenDetail): ResponseEntity<CrewMemberRankingResponse> {
        val rankings: List<CrewMemberRankingResult> = crewProxy.searchMemberRankings(
            my.crewId ?: throw AuthorizationException("사용자가 크루에 소속되어 있지 않습니다.")
        )
        val response = rankings.toCrewMemberRankingResponse()
        return ResponseEntity.ok(response)
    }
}
