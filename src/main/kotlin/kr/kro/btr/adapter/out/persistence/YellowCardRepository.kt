package kr.kro.btr.adapter.out.persistence

import kr.kro.btr.domain.entity.YellowCardEntity
import kr.kro.btr.domain.entity.YellowCardMultiKey
import org.springframework.data.jpa.repository.JpaRepository

interface YellowCardRepository : JpaRepository<YellowCardEntity, YellowCardMultiKey> {
    fun existsBySourceUserIdAndTargetUserId(sourceUserId: Long, targetUserId: Long): Boolean
}