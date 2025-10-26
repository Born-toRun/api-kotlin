package kr.kro.btr.adapter.out.persistence

import kr.kro.btr.domain.entity.ActivityImageMappingEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ActivityImageMappingRepository : JpaRepository<ActivityImageMappingEntity, Long> {
    fun deleteAllByImageIdIn(imageIds: List<Long>)
}