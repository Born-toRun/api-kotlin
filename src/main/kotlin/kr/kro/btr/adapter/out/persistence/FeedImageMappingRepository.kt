package kr.kro.btr.adapter.out.persistence

import kr.kro.btr.domain.entity.FeedImageMappingEntity
import org.springframework.data.jpa.repository.JpaRepository

interface FeedImageMappingRepository : JpaRepository<FeedImageMappingEntity, Long> {
    fun deleteAllByImageIdIn(imageIds: List<Long>)
}
