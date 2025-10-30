package kr.kro.btr.infrastructure

import kr.kro.btr.adapter.out.persistence.AnnounceRepository
import kr.kro.btr.base.extension.findByIdOrThrow
import kr.kro.btr.base.extension.toAnnounceEntity
import kr.kro.btr.domain.entity.AnnounceEntity
import kr.kro.btr.domain.model.ModifyAnnounceQuery
import kr.kro.btr.infrastructure.model.CreateAnnounceQuery
import org.springframework.stereotype.Component

@Component
class AnnounceGateway(
    private val repository: AnnounceRepository
) {

    fun create(query: CreateAnnounceQuery) {
        val announceEntity = query.toAnnounceEntity()
        repository.save(announceEntity)
    }

    fun searchAll(): List<AnnounceEntity> {
        return repository.findAll()
    }

    fun detail(announceId: Long): AnnounceEntity {
        return repository.findByIdOrThrow(announceId)
    }

    fun modify(query: ModifyAnnounceQuery): AnnounceEntity {
        val announceEntity = detail(query.id)
        announceEntity.modify(query)
        return repository.save(announceEntity)
    }

    fun remove(announceId: Long) {
        repository.deleteById(announceId)
    }
}
