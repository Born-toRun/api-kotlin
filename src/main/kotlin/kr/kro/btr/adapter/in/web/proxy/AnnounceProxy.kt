package kr.kro.btr.adapter.`in`.web.proxy

import kr.kro.btr.adapter.`in`.web.payload.CreateAnnounceRequest
import kr.kro.btr.adapter.`in`.web.payload.ModifyAnnounceRequest
import kr.kro.btr.base.extension.toCreateAnnounceCommand
import kr.kro.btr.base.extension.toModifyAnnounceCommand
import kr.kro.btr.domain.port.AnnouncePort
import kr.kro.btr.domain.port.model.result.AnnounceResult
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component

@Component
@CacheConfig(cacheNames = ["announce"])
class AnnounceProxy(
    private val port: AnnouncePort
) {

    @CacheEvict(allEntries = true)
    fun create(request: CreateAnnounceRequest, userId: Long) {
        val command = request.toCreateAnnounceCommand(userId)
        port.create(command)
    }

    @Cacheable(key = "'searchAll'")
    fun searchAll(): List<AnnounceResult> {
        return port.searchAll()
    }

    @Cacheable(key = "'detail: ' + #announceId")
    fun detail(announceId: Long): AnnounceResult {
        return port.detail(announceId)
    }

    @CacheEvict(allEntries = true)
    fun modify(request: ModifyAnnounceRequest, announceId: Long): AnnounceResult {
        val command = request.toModifyAnnounceCommand(announceId)
        return port.modify(command)
    }

    @CacheEvict(allEntries = true)
    fun remove(announceId: Long) {
        return port.remove(announceId)
    }
}
