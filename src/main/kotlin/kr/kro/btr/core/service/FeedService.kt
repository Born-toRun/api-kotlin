package kr.kro.btr.core.service

import kr.kro.btr.core.converter.FeedConverter
import kr.kro.btr.domain.entity.FeedImageMappingEntity
import kr.kro.btr.domain.port.FeedPort
import kr.kro.btr.domain.port.model.CreateFeedCommand
import kr.kro.btr.domain.port.model.FeedCard
import kr.kro.btr.domain.port.model.FeedResult
import kr.kro.btr.domain.port.model.ModifyFeedCommand
import kr.kro.btr.domain.port.model.RemoveFeedCommand
import kr.kro.btr.domain.port.model.SearchAllFeedCommand
import kr.kro.btr.domain.port.model.SearchFeedDetailCommand
import kr.kro.btr.infrastructure.FeedGateway
import kr.kro.btr.infrastructure.FeedImageMappingGateway
import kr.kro.btr.infrastructure.UserGateway
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class FeedService(
    private val feedConverter: FeedConverter,
    private val feedGateway: FeedGateway,
    private val feedImageMappingGateway: FeedImageMappingGateway,
    private val userGateway: UserGateway
) : FeedPort {

    @Transactional(readOnly = true)
    override fun searchDetail(command: SearchFeedDetailCommand): FeedResult {
        val feedEntity = feedGateway.search(command.feedId)
        return feedConverter.map(feedEntity, command.my)
    }

    @Transactional(readOnly = true)
    override fun searchAll(command: SearchAllFeedCommand, pageable: Pageable): Page<FeedCard> {
        val searchedUserIds: List<Long> = command.searchKeyword
            ?.let { keyword ->
                userGateway.searchByUserName(keyword)
                    .map { it.id }
            } ?: emptyList()

        val query = feedConverter.map(command, searchedUserIds)
        val feedPage = feedGateway.searchAllByFilter(query, pageable)

        return feedPage.map { entity ->
            feedConverter.map(entity, command.my.id)
        }
    }

    @Async
    @Transactional
    override fun increaseViewQty(feedId: Long) {
        feedGateway.increaseViewQty(feedId)
    }

    @Transactional
    override fun create(command: CreateFeedCommand) {
        val query = feedConverter.map(command)
        feedGateway.create(query)
    }

    @Transactional
    override fun remove(command: RemoveFeedCommand) {
        feedGateway.remove(command.feedId)
    }

    @Transactional
    override fun modify(command: ModifyFeedCommand) {
        val query = feedConverter.map(command)
        val modified = feedGateway.modify(query)

        val removedImageIds = modified.feedImageMappingEntities
            .map(FeedImageMappingEntity::imageId)
            .filter { command.imageIds?.contains(it) != true  }

        feedImageMappingGateway.removeAllByFileId(removedImageIds)
    }
}
