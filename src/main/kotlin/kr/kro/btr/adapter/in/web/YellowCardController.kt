package kr.kro.btr.adapter.`in`.web

import jakarta.validation.Valid
import kr.kro.btr.adapter.`in`.web.payload.CreateYellowCardRequest
import kr.kro.btr.adapter.`in`.web.proxy.YellowCardProxy
import kr.kro.btr.support.TokenDetail
import kr.kro.btr.support.annotation.AuthUser
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/yellow-cards")
class YellowCardController(private val yellowCardProxy: YellowCardProxy) {

    @PostMapping(value = [""], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun createYellowCard(@AuthUser my: TokenDetail, @RequestBody @Valid request: CreateYellowCardRequest) {
        yellowCardProxy.create(my.id, request)
    }
}
