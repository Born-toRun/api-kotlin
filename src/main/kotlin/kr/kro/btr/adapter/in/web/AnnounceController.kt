package kr.kro.btr.adapter.`in`.web

import jakarta.validation.Valid
import kr.kro.btr.adapter.`in`.web.payload.CreateAnnounceRequest
import kr.kro.btr.adapter.`in`.web.payload.DetailAnnounceResponse
import kr.kro.btr.adapter.`in`.web.payload.ModifyAnnounceRequest
import kr.kro.btr.adapter.`in`.web.payload.ModifyAnnounceResponse
import kr.kro.btr.adapter.`in`.web.proxy.AnnounceProxy
import kr.kro.btr.base.extension.toDetailAnnounceResponse
import kr.kro.btr.base.extension.toModifyAnnounceResponse
import kr.kro.btr.support.TokenDetail
import kr.kro.btr.support.annotation.AuthUser
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/announces")
class AnnounceController(
    private val proxy: AnnounceProxy
) {

    @PostMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun create(@AuthUser my: TokenDetail, @RequestBody @Valid request: CreateAnnounceRequest): ResponseEntity<Void> {
        proxy.create(request, my.id)
        return ResponseEntity(CREATED)
    }

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun searchAll(): ResponseEntity<Void> {
        proxy.searchAll()
        return ResponseEntity(CREATED)
    }

    @GetMapping("/{announceId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun detail(@PathVariable announceId: Long): ResponseEntity<DetailAnnounceResponse> {
        val announceResult = proxy.detail(announceId)
        val response = announceResult.toDetailAnnounceResponse()
        return ResponseEntity.ok(response)
    }

    @PutMapping("/{announceId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun modify(@PathVariable announceId: Long, @RequestBody request: ModifyAnnounceRequest): ResponseEntity<ModifyAnnounceResponse> {
        val announceResult = proxy.modify(request, announceId)
        val response = announceResult.toModifyAnnounceResponse()
        return ResponseEntity.ok(response)
    }

    @DeleteMapping("/{announceId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun modify(@PathVariable announceId: Long): ResponseEntity<Void> {
        proxy.remove(announceId)
        return ResponseEntity.ok().build()
    }
}
