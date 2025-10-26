package kr.kro.btr.adapter.`in`.web

import jakarta.validation.Valid
import kr.kro.btr.adapter.`in`.web.payload.CreateCrewRequest
import kr.kro.btr.adapter.`in`.web.payload.DetailCrewResponse
import kr.kro.btr.adapter.`in`.web.payload.SearchCrewsResponse
import kr.kro.btr.adapter.`in`.web.proxy.CrewProxy
import kr.kro.btr.base.extension.toDetailCrewResponse
import kr.kro.btr.base.extension.toSearchCrewResponse
import kr.kro.btr.domain.port.model.result.CrewResult
import kr.kro.btr.support.TokenDetail
import kr.kro.btr.support.annotation.AuthUser
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
    fun detailMyCrew(@AuthUser my: TokenDetail): ResponseEntity<DetailCrewResponse> {
        val crew = crewProxy.detailMyCrew(my.crewId)
        val response = crew.toDetailCrewResponse()
        return ResponseEntity.ok(response)
    }

    @PostMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun create(@RequestBody @Valid request: CreateCrewRequest): ResponseEntity<Void> {
        crewProxy.create(request)
        return ResponseEntity(CREATED)
    }
}
