package kr.kro.btr.infrastructure

import kr.kro.btr.adapter.out.persistence.CommentRepository
import kr.kro.btr.adapter.out.persistence.FeedImageMappingRepository
import kr.kro.btr.adapter.out.persistence.FeedRepository
import kr.kro.btr.adapter.out.persistence.RecommendationRepository
import kr.kro.btr.adapter.out.persistence.querydsl.FeedQuery
import kr.kro.btr.base.extension.toFeedEntity
import kr.kro.btr.domain.constant.RecommendationType
import kr.kro.btr.domain.entity.FeedEntity
import kr.kro.btr.domain.entity.FeedImageMappingEntity
import kr.kro.btr.domain.model.ModifyFeedQuery
import kr.kro.btr.infrastructure.model.CreateFeedQuery
import kr.kro.btr.infrastructure.model.FeedAggregationData
import kr.kro.btr.infrastructure.model.SearchAllFeedQuery
import kr.kro.btr.support.exception.NotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component

@Component
class FeedGateway(
    private val feedRepository: FeedRepository,
    private val feedImageMappingRepository: FeedImageMappingRepository,
    private val feedQuery: FeedQuery,
    private val recommendationRepository: RecommendationRepository,
    private val commentRepository: CommentRepository
) {

    fun searchAllByFilter(query: SearchAllFeedQuery, pageable: Pageable): Page<FeedAggregationData> {
        val feedPage = feedQuery.searchAllByFilter(query, pageable)

        if (feedPage.content.isEmpty()) {
            return PageImpl(emptyList(), pageable, 0)
        }

        val feedIds = feedPage.content.map { it.id }
        val userId = query.my?.id ?: 0L

        val recommendationCounts = recommendationRepository
            .countGroupByContentId(feedIds, RecommendationType.FEED)
            .associate { it.getContentId() to it.getCount() }

        val userRecommendedFeedIds = if (userId > 0) {
            recommendationRepository
                .findUserRecommendedContentIds(userId, feedIds, RecommendationType.FEED)
                .toSet()
        } else {
            emptySet()
        }

        val commentCounts = commentRepository
            .countGroupByFeedId(feedIds)
            .associate { it.getFeedId() to it.getCount() }

        val userCommentedFeedIds = if (userId > 0) {
            commentRepository
                .findUserCommentedFeedIds(userId, feedIds)
                .toSet()
        } else {
            emptySet()
        }

        val aggregatedResults = feedPage.content.map { feed ->
            FeedAggregationData(
                feedEntity = feed,
                recommendationCount = recommendationCounts[feed.id]?.toInt() ?: 0,
                hasUserRecommendation = feed.id in userRecommendedFeedIds,
                commentCount = commentCounts[feed.id]?.toInt() ?: 0,
                hasUserComment = feed.id in userCommentedFeedIds
            )
        }

        return PageImpl(aggregatedResults, pageable, feedPage.totalElements)
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

        feedEntity.contents = query.contents
        feedEntity.category = query.category
        feedEntity.accessLevel = query.accessLevel

        if (query.imageIds != null) {
            val existingImageIds = feedEntity.feedImageMappingEntities
                .map { it.imageId }
                .toSet()
            val newImageIds = query.imageIds.toSet()
            val toRemove = feedEntity.feedImageMappingEntities
                .filter { it.imageId !in newImageIds }

            if (toRemove.isNotEmpty()) {
                feedEntity.feedImageMappingEntities.removeAll(toRemove.toSet())
                feedImageMappingRepository.deleteAll(toRemove)
            }

            val toAdd = newImageIds
                .filter { it !in existingImageIds }
                .map { imageId ->
                    FeedImageMappingEntity(
                        imageId = imageId,
                        feedId = feedEntity.id
                    )
                }

            if (toAdd.isNotEmpty()) {
                val savedMappings = feedImageMappingRepository.saveAll(toAdd)
                feedEntity.feedImageMappingEntities.addAll(savedMappings)
            }
        }

        return feedRepository.save(feedEntity)
    }

    fun search(feedId: Long): FeedEntity {
        return feedRepository.findByIdOrNull(feedId)
            ?: throw NotFoundException("해당 피드를 찾을 수 없습니다.")
    }

    fun searchWithAggregation(feedId: Long, userId: Long): FeedAggregationData {
        val feed = search(feedId)
        val recommendationCount = recommendationRepository
            .countByContentIdAndRecommendationType(feedId, RecommendationType.FEED)
        val hasUserRecommendation = if (userId > 0) {
            recommendationRepository
                .existsByContentIdAndUserIdAndRecommendationType(feedId, userId, RecommendationType.FEED)
        } else {
            false
        }
        val commentCount = commentRepository.countByFeedId(feedId)
        val hasUserComment = if (userId > 0) {
            commentRepository.existsByFeedIdAndUserId(feedId, userId)
        } else {
            false
        }

        return FeedAggregationData(
            feedEntity = feed,
            recommendationCount = recommendationCount,
            hasUserRecommendation = hasUserRecommendation,
            commentCount = commentCount,
            hasUserComment = hasUserComment
        )
    }

    fun searchMyFeedsWithAggregation(myUserId: Long): List<FeedAggregationData> {
        val feedEntities = feedRepository.findAllByUserIdWithImages(myUserId)

        if (feedEntities.isEmpty()) {
            return emptyList()
        }

        val feedIds = feedEntities.map { it.id }

        val recommendationCounts = recommendationRepository
            .countGroupByContentId(feedIds, RecommendationType.FEED)
            .associate { it.getContentId() to it.getCount() }

        val userRecommendedFeedIds = recommendationRepository
            .findUserRecommendedContentIds(myUserId, feedIds, RecommendationType.FEED)
            .toSet()

        val commentCounts = commentRepository
            .countGroupByFeedId(feedIds)
            .associate { it.getFeedId() to it.getCount() }

        val userCommentedFeedIds = commentRepository
            .findUserCommentedFeedIds(myUserId, feedIds)
            .toSet()

        return feedEntities.map { feed ->
            FeedAggregationData(
                feedEntity = feed,
                recommendationCount = recommendationCounts[feed.id]?.toInt() ?: 0,
                hasUserRecommendation = feed.id in userRecommendedFeedIds,
                commentCount = commentCounts[feed.id]?.toInt() ?: 0,
                hasUserComment = feed.id in userCommentedFeedIds
            )
        }
    }
}
