package kr.kro.btr.infrastructure

import kr.kro.btr.adapter.out.persistence.ActivityImageMappingRepository
import org.springframework.stereotype.Component

@Component
class ActivityImageMappingGateway(private val activityImageMappingRepository: ActivityImageMappingRepository) {

    fun removeAllByFileId(imageIds: List<Long>) {
        activityImageMappingRepository.deleteAllByImageIdIn(imageIds)
    }
}