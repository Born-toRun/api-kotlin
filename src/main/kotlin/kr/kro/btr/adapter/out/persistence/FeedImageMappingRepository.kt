package kr.kro.btr.adapter.out.persistence

import kr.kro.btr.domain.entity.FeedImageMappingEntity
import org.springframework.data.jpa.repository.JpaRepository

interface FeedImageMappingRepository : JpaRepository<FeedImageMappingEntity, Long> {
    // List<FeedImageMappingEntity> findAllByFeedIdIn(final List<Long> feedIds);
    // List<FeedImageMappingEntity> findAllByImageIdIn(final List<Long> imageIds);
    fun deleteAllByImageIdIn(imageIds: List<Long>)
}