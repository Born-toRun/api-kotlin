package kr.kro.btr.core.converter

import kr.kro.btr.adapter.`in`.web.payload.CreateFeedRequest
import kr.kro.btr.adapter.`in`.web.payload.DetailFeedResponse
import kr.kro.btr.adapter.`in`.web.payload.ModifyFeedRequest
import kr.kro.btr.adapter.`in`.web.payload.SearchFeedRequest
import kr.kro.btr.adapter.`in`.web.payload.SearchFeedResponse
import kr.kro.btr.domain.entity.FeedEntity
import kr.kro.btr.domain.port.model.CreateFeedCommand
import kr.kro.btr.domain.port.model.FeedCard
import kr.kro.btr.domain.port.model.FeedResult
import kr.kro.btr.domain.port.model.ModifyFeedCommand
import kr.kro.btr.domain.port.model.RemoveFeedCommand
import kr.kro.btr.domain.port.model.SearchAllFeedCommand
import kr.kro.btr.domain.port.model.SearchFeedDetailCommand
import kr.kro.btr.infrastructure.model.CreateFeedQuery
import kr.kro.btr.infrastructure.model.ModifyFeedQuery
import kr.kro.btr.infrastructure.model.SearchAllFeedQuery
import kr.kro.btr.support.TokenDetail
import org.springframework.stereotype.Component
import kotlin.Long
import kotlin.collections.List

@Component
class FeedConverter {

    fun map(source: FeedResult): DetailFeedResponse {
        return DetailFeedResponse(
            id = source.id,
            contents = source.contents,
            images = map(source.images),
            category = source.category,
            accessLevel = source.accessLevel,
            viewQty = source.viewQty,
            recommendationQty = source.recommendationQty,
            commentQty = source.commentQty,
            registeredAt = source.registeredAt,
            writer = DetailFeedResponse.Writer(
                userId = source.writer.userId,
                userName = source.writer.userName,
                crewName = source.writer.crewName,
                profileImageUri = source.writer.profileImageUri,
                isAdmin = source.writer.isAdmin,
                isManager = source.writer.isManager
            ),
            viewer = DetailFeedResponse.Viewer(
                hasMyRecommendation = source.hasMyRecommendation,
                hasMyComment = source.hasMyComment
            )
        )
    }

    fun map(source: FeedCard): SearchFeedResponse {
        return SearchFeedResponse(
            id = source.id,
            imageUris = source.imageUris,
            contents = source.contents,
            viewQty = source.viewQty,
            recommendationQty = source.recommendationQty,
            commentQty = source.commentQty,
            registeredAt = source.registeredAt,
            writer = SearchFeedResponse.Writer(
                userName = source.writer.userName,
                crewName = source.writer.crewName,
                profileImageUri = source.writer.profileImageUri,
                isAdmin = source.writer.isAdmin,
                isManager = source.writer.isManager
            ),
            viewer = SearchFeedResponse.Viewer(
                hasMyRecommendation = source.hasRecommendation,
                hasMyComment = source.hasComment
            )
        )
    }

    fun map(my: TokenDetail, feedId: Long): SearchFeedDetailCommand {
        return SearchFeedDetailCommand(
            my = my,
            feedId = feedId,
        )
    }

    fun map(source: SearchFeedRequest, my: TokenDetail, feedId: Long): SearchAllFeedCommand {
        return SearchAllFeedCommand(
            category = source.category,
            searchKeyword = source.searchKeyword,
            isMyCrew = source.isMyCrew,
            my = my,
            lastFeedId = feedId,
        )
    }

    fun map(source: CreateFeedRequest, my: TokenDetail): CreateFeedCommand {
        return CreateFeedCommand(
            imageIds = source.imageIds,
            contents = source.contents,
            category = source.category,
            accessLevel = source.accessLevel,
            myUserId = my.id
        )
    }

    fun mapToRemoveFeedCommand(my: TokenDetail, feedId: Long): RemoveFeedCommand {
        return RemoveFeedCommand(
            feedId = feedId,
            my = my
        )
    }

    fun map(source: ModifyFeedRequest, feedId: Long): ModifyFeedCommand {
        return ModifyFeedCommand(
            imageIds = source.imageIds,
            contents = source.contents,
            category = source.category,
            accessLevel = source.accessLevel,
            feedId = feedId
        )
    }

    fun map(source: CreateFeedQuery): FeedEntity{
        return FeedEntity(
            userId = source.userId,
            contents = source.contents,
            category = source.category,
            accessLevel = source.accessLevel,
        )
    }

    fun map(source: List<FeedResult.Image>?): List<DetailFeedResponse.Image>? {
        return source?.map { image ->
            DetailFeedResponse.Image(
                imageId = image.id,
                imageUri = image.imageUri
            )
        }
    }

    fun map(source: FeedEntity, my: TokenDetail): FeedResult {
        val writer = source.userEntity!!
        val images = if (source.feedImageMappingEntities.isNotEmpty()) {
            source.feedImageMappingEntities.map { image ->
                FeedResult.Image(
                    id = image.objectStorageEntity!!.id,
                    imageUri = image.objectStorageEntity!!.fileUri
                )
            }
        } else {
            null
        }
        return FeedResult(
            id = source.id,
            contents = source.contents,
            category = source.category,
            accessLevel = source.accessLevel,
            viewQty = source.viewQty,
            registeredAt = source.registeredAt,
            updatedAt = source.updatedAt,

            images = images,
            writer = FeedResult.Writer(
                userId = writer.id,
                userName = writer.name!!,
                crewName = writer.crewEntity!!.name,
                profileImageUri = writer.getProfileImageUri(),
                isAdmin = writer.getIsAdmin(),
                isManager = writer.getIsManager()
            ),
            recommendationQty = source.recommendationEntities.size,
            hasMyRecommendation = source.hasMyRecommendation(my.id),
            commentQty = source.commentEntities.size,
            hasMyComment = source.hasMyComment(my.id)
        )
    }

    fun map(source: SearchAllFeedCommand, userIds: List<Long>): SearchAllFeedQuery {
        return SearchAllFeedQuery(
            category = source.category,
            searchKeyword = source.searchKeyword,
            isMyCrew = source.isMyCrew,
            my = source.my,
            lastFeedId = source.lastFeedId,
            searchedUserIds = userIds,
        )
    }

    fun map(source: FeedEntity, userId: Long): FeedCard {
        val writer = source.userEntity!!
        return FeedCard(
            id = source.id,
            imageUris = source.getImageUris(),
            contents = source.contents,
            viewQty = source.viewQty,
            recommendationQty = source.getRecommendationQty(),
            commentQty = source.getCommentQty(),
            registeredAt = source.registeredAt,
            hasRecommendation = source.hasMyRecommendation(userId),
            hasComment = source.hasMyComment(userId),

            writer = FeedCard.Writer(
                userName = writer.name!!,
                crewName = writer.crewEntity!!.name,
                profileImageUri = writer.getProfileImageUri(),
                isAdmin = writer.getIsAdmin(),
                isManager = writer.getIsManager(),
            ),
        )
    }

    fun map(source: CreateFeedCommand): CreateFeedQuery {
        return CreateFeedQuery(
            contents = source.contents,
            imageIds = source.imageIds,
            category = source.category,
            accessLevel = source.accessLevel,
            userId = source.myUserId
        )
    }

    fun map(source: ModifyFeedCommand): ModifyFeedQuery {
        return ModifyFeedQuery(
            feedId = source.feedId,
            imageIds = source.imageIds,
            contents = source.contents,
            category = source.category,
            accessLevel = source.accessLevel
        )
    }
}
