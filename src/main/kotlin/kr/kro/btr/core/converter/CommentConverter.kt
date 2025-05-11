package kr.kro.btr.core.converter

import kr.kro.btr.adapter.`in`.web.payload.CreateCommentRequest
import kr.kro.btr.adapter.`in`.web.payload.ModifyCommentRequest
import kr.kro.btr.adapter.`in`.web.payload.ModifyCommentResponse
import kr.kro.btr.adapter.`in`.web.payload.SearchCommentDetailResponse
import kr.kro.btr.adapter.`in`.web.payload.SearchCommentResponse
import kr.kro.btr.domain.entity.CommentEntity
import kr.kro.btr.domain.port.model.CommentDetail
import kr.kro.btr.domain.port.model.CommentResult
import kr.kro.btr.domain.port.model.CreateCommentCommand
import kr.kro.btr.domain.port.model.ModifyCommentCommand
import kr.kro.btr.infrastructure.model.CreateCommentQuery
import kr.kro.btr.infrastructure.model.ModifyCommentQuery
import org.springframework.stereotype.Component
import kotlin.Long

@Component
class CommentConverter {

    fun mapToSearchCommentResponse(source: List<CommentResult>): SearchCommentResponse {
        val comments = source.map { commentResult ->
            SearchCommentResponse.Comment(
                id = commentResult.id,
                parentId = commentResult.parentId,
                reCommentQty = commentResult.reCommentQty,
                writer = map(commentResult.writer),
                contents = commentResult.contents,
                registeredAt = commentResult.registeredAt,
                isMyComment = commentResult.isMyComment
            )
        }

        return SearchCommentResponse(comments)
    }

    fun map(source: CommentDetail): SearchCommentDetailResponse {
        return SearchCommentDetailResponse(
            id = source.id,
            writer = map(source.writer),
            contents = source.contents,
            registeredAt = source.registeredAt,
            reComments = map(source.reCommentResults)
        )
    }

    fun map(source: CommentResult): ModifyCommentResponse {
        return ModifyCommentResponse(
            id = source.id,
            contents = source.contents
        )
    }

    fun map(source: CommentResult.Writer): SearchCommentResponse.Writer {
        return SearchCommentResponse.Writer(
            userId = source.userId,
            userName = source.userName,
            profileImageUri = source.profileImageUri,
            crewName = source.crewName,
            isAdmin = source.isAdmin,
            isManager = source.isManager
        )
    }

    fun map(source: CommentDetail.Writer): SearchCommentDetailResponse.Writer {
        return SearchCommentDetailResponse.Writer(
            userId = source.userId,
            userName = source.userName,
            profileImageUri = source.profileImageUri,
            crewName = source.crewName,
            isAdmin = source.isAdmin,
            isManager = source.isManager
        )
    }

    fun mapToReCommentWriter(source: CommentResult.Writer): SearchCommentDetailResponse.ReComment.Writer {
        return SearchCommentDetailResponse.ReComment.Writer(
            userId = source.userId,
            userName = source.userName,
            profileImageUri = source.profileImageUri,
            crewName = source.crewName,
            isAdmin = source.isAdmin,
            isManager = source.isManager
        )
    }

    fun map(source: List<CommentResult>): List<SearchCommentDetailResponse.ReComment> {
        return source.map { commentResult ->
            SearchCommentDetailResponse.ReComment(
                id = commentResult.id,
                contents = commentResult.contents,
                registeredAt = commentResult.registeredAt,
                writer = mapToReCommentWriter(commentResult.writer),
                isMyComment = commentResult.isMyComment
            )
        }
    }

    fun map(source: CreateCommentRequest, userId: Long, feedId: Long): CreateCommentCommand {
        return CreateCommentCommand(
            myUserId = userId,
            feedId = feedId,
            parentCommentId = source.parentCommentId,
            contents = source.contents
        )
    }

    fun map(source: ModifyCommentRequest, commentId: Long): ModifyCommentCommand {
        return ModifyCommentCommand(
            commentId = commentId,
            contents = source.contents
        )
    }

    fun map(source: CommentEntity, userId: Long): CommentResult {
        val writer = source.userEntity!!
        return CommentResult(
            id = source.id,
            parentId = source.parentId,
            reCommentQty = source.child.size,
            feedId = source.feedId,
            contents = source.contents,
            registeredAt = source.registeredAt,
            updatedAt = source.updatedAt,
            isMyComment = source.userId == userId,
            writer = CommentResult.Writer(
                userId = writer.id,
                userName = writer.name!!,
                profileImageUri = writer.getProfileImageUri(),
                crewName = writer.crewEntity!!.name,
                isAdmin = writer.getIsAdmin(),
                isManager = writer.getIsManager()
            )
        )
    }

    fun map(source: CommentEntity): CommentResult {
        val writer = source.userEntity!!
        return CommentResult(
            id = source.id,
            parentId = source.parentId,
            feedId = source.feedId,
            contents = source.contents,
            registeredAt = source.registeredAt,
            updatedAt = source.updatedAt,
            writer = CommentResult.Writer(
                userId = writer.id,
                userName = writer.name!!,
                profileImageUri = writer.getProfileImageUri(),
                crewName = writer.crewEntity!!.name,
                isAdmin = writer.getIsAdmin(),
                isManager = writer.getIsManager()
            )
        )
    }

    fun map(source: List<CommentEntity>, userId: Long): List<CommentResult> {
        return source.map { commentEntity ->
            map(commentEntity, userId)
        }
    }

    fun map(commentEntity: CommentEntity, commentResults: List<CommentResult>): CommentDetail {
        val writer = commentEntity.userEntity!!
        return CommentDetail(
            id = commentEntity.id,
            parentId = commentEntity.parentId,
            feedId = commentEntity.feedId,
            contents = commentEntity.contents,
            registeredAt = commentEntity.registeredAt,
            updatedAt = commentEntity.updatedAt,
            reCommentResults = commentResults,
            writer = CommentDetail.Writer(
                userId = writer.id,
                userName = writer.name!!,
                profileImageUri = writer.getProfileImageUri(),
                crewName = writer.crewEntity!!.name,
                isAdmin = writer.getIsAdmin(),
                isManager = writer.getIsManager()
            )
        )
    }

    fun map(source: CreateCommentCommand): CreateCommentQuery {
        return CreateCommentQuery(
            myUserId = source.myUserId,
            feedId = source.feedId,
            parentCommentId = source.parentCommentId,
            contents = source.contents
        )
    }

    fun map(source: ModifyCommentCommand): ModifyCommentQuery {
        return ModifyCommentQuery(
            commentId = source.commentId,
            contents = source.contents
        )
    }

    fun map(source: CreateCommentQuery): CommentEntity {
        return CommentEntity(
            userId = source.myUserId,
            feedId = source.feedId,
            parentId = source.parentCommentId,
            contents = source.contents
        )
    }
}
