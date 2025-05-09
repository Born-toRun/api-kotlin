package kr.kro.btr.infrastructure

import kr.kro.btr.adapter.out.persistence.YellowCardRepository
import kr.kro.btr.core.converter.YellowCardConverter
import kr.kro.btr.infrastructure.model.CreateYellowCardQuery
import org.springframework.stereotype.Component

@Component
class YellowCardGateway(
    private val yellowCardConverter: YellowCardConverter,
    private val yellowCardRepository: YellowCardRepository
) {

    fun create(query: CreateYellowCardQuery) {
        val yellowCardEntity = yellowCardConverter.map(query)
        yellowCardRepository.save(yellowCardEntity)
    }
}
