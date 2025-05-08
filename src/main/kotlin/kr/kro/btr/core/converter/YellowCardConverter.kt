package kr.kro.btr.core.converter

import kr.kro.btr.adapter.`in`.web.payload.CreateYellowCardRequest
import kr.kro.btr.domain.entity.YellowCardEntity
import kr.kro.btr.domain.port.model.CreateYellowCardCommand
import kr.kro.btr.infrastructure.model.CreateYellowCardQuery
import org.springframework.stereotype.Component

@Component
class YellowCardConverter {

    fun map(source: CreateYellowCardRequest, userId: Long): CreateYellowCardCommand {
        return CreateYellowCardCommand(
            targetUserId = source.targetUserId,
            sourceUserId = userId,
            reason = source.reason,
            basis = source.basis
        )
    }

    fun map(source: CreateYellowCardCommand): CreateYellowCardQuery {
        return CreateYellowCardQuery(
            targetUserId = source.targetUserId,
            sourceUserId = source.sourceUserId,
            reason = source.reason,
            basis = source.basis
        )
    }

    fun map(source: CreateYellowCardQuery): YellowCardEntity {
        return YellowCardEntity(
            targetUserId = source.targetUserId,
            sourceUserId = source.sourceUserId,
            reason = source.reason,
            basis = source.basis
        )
    }
}
