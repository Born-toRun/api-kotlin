package kr.kro.btr.adapter.`in`.web.proxy

import kr.kro.btr.adapter.`in`.web.payload.CreateCrewRequest
import kr.kro.btr.base.extension.toCreateCrewCommand
import kr.kro.btr.domain.port.CrewPort
import kr.kro.btr.domain.port.model.Crew
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
    fun searchAll(): List<Crew> {
        return crewPort.searchAll()
    }

    @Cacheable(key = "'detail: ' + #crewId")
    fun detail(crewId: Long): Crew {
        return crewPort.detail(crewId)
    }

    @CacheEvict(allEntries = true)
    fun create(request: CreateCrewRequest) {
        val command = request.toCreateCrewCommand()
        crewPort.create(command)
    }
}
