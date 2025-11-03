package kr.kro.btr.infrastructure

import kr.kro.btr.adapter.out.persistence.FeedImageMappingRepository
import kr.kro.btr.adapter.out.persistence.FeedRepository
import kr.kro.btr.adapter.out.persistence.querydsl.FeedQuery
import kr.kro.btr.base.extension.toFeedEntity
import kr.kro.btr.domain.entity.FeedEntity
import kr.kro.btr.domain.entity.FeedImageMappingEntity
import kr.kro.btr.domain.model.ModifyFeedQuery
import kr.kro.btr.infrastructure.model.CreateFeedQuery
import kr.kro.btr.infrastructure.model.SearchAllFeedQuery
import kr.kro.btr.support.exception.NotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component

@Component
class FeedGateway(
    private val feedRepository: FeedRepository,
    private val feedImageMappingRepository: FeedImageMappingRepository,
    private val feedQuery: FeedQuery
) {

    fun searchAllByFilter(query: SearchAllFeedQuery, pageable: Pageable): Page<FeedEntity> {
        return feedQuery.searchAllByFilter(query, pageable)
    }

    fun increaseViewQty(feedId: Long) {
        feedQuery.increaseViewQty(feedId)
    }

    fun create(query: CreateFeedQuery) {
        val feedEntity = query.toFeedEntity()
        feedRepository.save(feedEntity)

        val feedImageMappingEntities = query.imageIds?.map { imageId ->
            FeedImageMappingEntity(
                imageId = imageId,
                feedId = feedEntity.id
            )
        }

        feedEntity.add(feedImageMappingEntities)
        if (feedImageMappingEntities != null) {
            feedImageMappingRepository.saveAll(feedImageMappingEntities)
        }
    }

    fun remove(feedId: Long) {
        feedRepository.deleteById(feedId)
    }

    fun modify(query: ModifyFeedQuery): FeedEntity {
        val feedEntity = search(query.feedId)

        val feedImageMappingEntities = query.imageIds?.map { imageId ->
            FeedImageMappingEntity(imageId = imageId)
        }

        feedEntity.modify(query)
        feedEntity.modify(feedImageMappingEntities)

        if (feedImageMappingEntities != null) {
            feedImageMappingRepository.saveAll(feedImageMappingEntities)
        }

        return feedRepository.save(feedEntity)
    }

    fun search(feedId: Long): FeedEntity {
        return feedRepository.findByIdOrNull(feedId)
            ?: throw NotFoundException("해당 피드를 찾을 수 없습니다.")
    }

    fun searchMyFeeds(myUserId: Long): List<FeedEntity> {
        return feedRepository.findAllByUserIdWithImages(myUserId)
    }
}
