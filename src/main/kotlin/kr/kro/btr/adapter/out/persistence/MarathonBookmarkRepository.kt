package kr.kro.btr.adapter.out.persistence

import kr.kro.btr.domain.entity.MarathonBookmarkEntity
import org.springframework.data.jpa.repository.JpaRepository

interface MarathonBookmarkRepository : JpaRepository<MarathonBookmarkEntity, Long> {
    fun findByUserIdAndMarathonId(userId: Long, marathonId: Long): MarathonBookmarkEntity?
}