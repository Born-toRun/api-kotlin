package kr.kro.btr.core.service

import kr.kro.btr.base.extension.toAnnounceResult
import kr.kro.btr.base.extension.toAnnounceResults
import kr.kro.btr.base.extension.toCreateAnnounceQuery
import kr.kro.btr.base.extension.toModifyAnnounceQuery
import kr.kro.btr.domain.port.AnnouncePort
import kr.kro.btr.domain.port.model.CreateAnnounceCommand
import kr.kro.btr.domain.port.model.ModifyAnnounceCommand
import kr.kro.btr.domain.port.model.result.AnnounceResult
import kr.kro.btr.infrastructure.AnnounceGateway
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AnnounceService(
    private val announceGateway: AnnounceGateway
) : AnnouncePort {

    @Transactional
    override fun create(command: CreateAnnounceCommand) {
        val query = command.toCreateAnnounceQuery()
        announceGateway.create(query)
    }

    @Transactional(readOnly = true)
    override fun searchAll(): List<AnnounceResult> {
        val announceEntities = announceGateway.searchAll()
        return announceEntities.toAnnounceResults()
    }

    @Transactional(readOnly = true)
    override fun detail(announceId: Long): AnnounceResult {
        val announceEntity = announceGateway.detail(announceId)
        return announceEntity.toAnnounceResult()
    }

    @Transactional
    override fun modify(command: ModifyAnnounceCommand): AnnounceResult {
        val query = command.toModifyAnnounceQuery()
        val announceEntity = announceGateway.modify(query)
        return announceEntity.toAnnounceResult()
    }

    @Transactional
    override fun remove(announceId: Long) {
        announceGateway.remove(announceId)
    }
}
