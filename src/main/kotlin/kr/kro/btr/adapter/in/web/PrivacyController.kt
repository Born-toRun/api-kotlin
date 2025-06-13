package kr.kro.btr.adapter.`in`.web

import jakarta.validation.Valid
import kr.kro.btr.adapter.`in`.web.payload.DetailUserPrivacyResponse
import kr.kro.btr.adapter.`in`.web.payload.SettingUserPrivacyRequest
import kr.kro.btr.adapter.`in`.web.proxy.PrivacyProxy
import kr.kro.btr.base.extension.toSearchUserPrivacyResponse
import kr.kro.btr.domain.port.model.result.UserPrivacyResult
import kr.kro.btr.support.TokenDetail
import kr.kro.btr.support.annotation.AuthUser
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/privacy")
class PrivacyController(
    private val privacyProxy: PrivacyProxy
) {

    @PutMapping("/users", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun settingUserPrivacy(@AuthUser my: TokenDetail, @RequestBody @Valid request: SettingUserPrivacyRequest): ResponseEntity<Void> {
        privacyProxy.modifyUserPrivacy(request, my.id)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/users", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getUserPrivacy(@AuthUser my: TokenDetail): ResponseEntity<DetailUserPrivacyResponse> {
        val userPrivacyResult: UserPrivacyResult = privacyProxy.searchUserPrivacy(my.id)
        val response: DetailUserPrivacyResponse = userPrivacyResult.toSearchUserPrivacyResponse()
        return ResponseEntity.ok(response)
    }
}
