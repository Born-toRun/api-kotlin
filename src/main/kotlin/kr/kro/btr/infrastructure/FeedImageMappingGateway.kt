package kr.kro.btr.infrastructure

import kr.kro.btr.adapter.out.persistence.FeedImageMappingRepository
import org.springframework.stereotype.Component

@Component
class FeedImageMappingGateway(private val feedImageMappingRepository: FeedImageMappingRepository) {

    fun removeAllByFileId(imageIds: List<Long>) {
        feedImageMappingRepository.deleteAllByImageIdIn(imageIds)
    }
}
