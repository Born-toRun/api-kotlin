package kr.kro.btr.adapter.out.persistence

import kr.kro.btr.domain.entity.ActivityEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ActivityRepository : JpaRepository<ActivityEntity, Long> {
    fun findAllByUserId(userId: Long): List<ActivityEntity>
}
