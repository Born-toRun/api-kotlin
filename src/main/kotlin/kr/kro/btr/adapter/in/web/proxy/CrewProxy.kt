package kr.kro.btr.adapter.`in`.web.proxy

import kr.kro.btr.adapter.`in`.web.payload.CreateCrewRequest
import kr.kro.btr.adapter.`in`.web.payload.ModifyCrewRequest
import kr.kro.btr.base.extension.toCreateCrewCommand
import kr.kro.btr.base.extension.toModifyCrewCommand
import kr.kro.btr.domain.port.CrewPort
import kr.kro.btr.domain.port.model.result.CrewMemberResult
import kr.kro.btr.domain.port.model.result.CrewResult
import kr.kro.btr.support.TokenDetail
import kr.kro.btr.support.exception.AuthorizationException
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component

@Component
@CacheConfig(cacheNames = ["crew"])
class CrewProxy(
    private val crewPort: CrewPort
) {

    @Cacheable(key = "'searchAll'")
    fun searchAll(): List<CrewResult> {
        return crewPort.searchAll()
    }

    @Cacheable(key = "'detail: ' + #crewId")
    fun detail(crewId: Long): CrewResult {
        return crewPort.detail(crewId)
    }

    @Cacheable(key = "'detail: ' + #crewId")
    fun detailMyCrew(crewId: Long?): CrewResult {
        return crewPort.detailMyCrew(crewId ?: throw AuthorizationException("사용자가 크루에 소속되어 있지 않습니다."))
    }

    @CacheEvict(allEntries = true)
    fun create(request: CreateCrewRequest) {
        val command = request.toCreateCrewCommand()
        crewPort.create(command)
    }

    @CacheEvict(allEntries = true)
    fun modify(request: ModifyCrewRequest, crewId: Long) {
        val command = request.toModifyCrewCommand(crewId)
        crewPort.modify(command)
    }

    @Cacheable(key = "'members: ' + #crewId")
    fun searchMembers(crewId: Long): List<CrewMemberResult> {
        return crewPort.searchMembers(crewId)
    }
}
