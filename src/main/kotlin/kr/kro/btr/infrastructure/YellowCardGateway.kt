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

    fun exists(sourceUserId: Long, targetUserId: Long): Boolean {
        return yellowCardRepository.existsBySourceUserIdAndTargetUserId(sourceUserId, targetUserId)
    }

    fun create(query: CreateYellowCardQuery) {
        val yellowCardEntity = yellowCardConverter.map(query)
        yellowCardRepository.save(yellowCardEntity)
    }
}
