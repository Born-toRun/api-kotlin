package kr.kro.btr.adapter.`in`.web

import jakarta.validation.Valid
import kr.kro.btr.adapter.`in`.web.payload.CreateCrewRequest
import kr.kro.btr.adapter.`in`.web.payload.DetailCrewResponse
import kr.kro.btr.adapter.`in`.web.payload.SearchCrewResponse
import kr.kro.btr.adapter.`in`.web.proxy.CrewProxy
import kr.kro.btr.core.converter.CrewConverter
import kr.kro.btr.domain.port.model.Crew
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/crews")
class CrewController(
    private val crewConverter: CrewConverter,
    private val crewProxy: CrewProxy
) {

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun searchAll(): ResponseEntity<SearchCrewResponse> {
        val crews: List<Crew> = crewProxy.searchAll()
        return ResponseEntity.ok(crewConverter.map(crews))
    }

    @GetMapping("/{crewId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun detail(@PathVariable crewId: Long): ResponseEntity<DetailCrewResponse> {
        val crew = crewProxy.detail(crewId)
        return ResponseEntity.ok(crewConverter.map(crew))
    }

    // TODO: 관리자 권한
    @PostMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun create(@RequestBody @Valid request: CreateCrewRequest): ResponseEntity<Void> {
        crewProxy.create(request)
        return ResponseEntity(CREATED)
    }
}
