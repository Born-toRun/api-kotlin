package kr.kro.btr.infrastructure

import kr.kro.btr.adapter.out.persistence.YellowCardRepository
import kr.kro.btr.base.extension.toYellowCardEntity
import kr.kro.btr.infrastructure.model.CreateYellowCardQuery
import org.springframework.stereotype.Component

@Component
class YellowCardGateway(
    private val yellowCardRepository: YellowCardRepository
) {

    fun create(query: CreateYellowCardQuery) {
        val yellowCardEntity = query.toYellowCardEntity()
        yellowCardRepository.save(yellowCardEntity)
    }
}
