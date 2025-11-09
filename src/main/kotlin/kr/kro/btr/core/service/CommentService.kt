package kr.kro.btr.core.service

import kr.kro.btr.base.extension.toCommentDetail
import kr.kro.btr.base.extension.toCommentResult
import kr.kro.btr.base.extension.toCreateCommentQuery
import kr.kro.btr.base.extension.toModifyCommentQuery
import kr.kro.btr.domain.entity.CommentEntity
import kr.kro.btr.domain.port.CommentPort
import kr.kro.btr.domain.port.model.CreateCommentCommand
import kr.kro.btr.domain.port.model.DetailCommentCommand
import kr.kro.btr.domain.port.model.ModifyCommentCommand
import kr.kro.btr.domain.port.model.SearchAllCommentCommand
import kr.kro.btr.domain.port.model.result.CommentDetailResult
import kr.kro.btr.domain.port.model.result.CommentResult
import kr.kro.btr.infrastructure.CommentGateway
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CommentService(
    private val commentGateway: CommentGateway
) : CommentPort {

    @Transactional(readOnly = true)
    override fun searchAll(command: SearchAllCommentCommand): List<CommentResult> {
        val commentEntities = commentGateway.searchAll(command.feedId)

        if (commentEntities.isEmpty()) return emptyList()

        val parentCommentIds = commentEntities
            .filter { it.isRootComment() }
            .map { it.id }

        val replyCountMap = commentGateway.getReplyCountMap(parentCommentIds)

        // 댓글/대댓글 정렬
        val sortedEntities = commentEntities.sortedWith(
            compareBy<CommentEntity>(
                { if (it.isRootComment()) it.id else it.parentId },
                { if (it.isRootComment()) 0L else -1L },
                { it.id }
            ).reversed()
        )

        return sortedEntities.map { comment ->
            if (comment.isRootComment()) {
                comment.toCommentResult(command.myUserId, replyCountMap[comment.id] ?: 0)
            } else {
                comment.toCommentResult(command.myUserId, 0)
            }
        }
    }

    @Transactional(readOnly = true)
    override fun detail(command: DetailCommentCommand): CommentDetailResult {
        val parentComment = commentGateway.searchWithReplies(command.commentId)

        val reCommentResults = commentGateway.searchReComments(command.commentId)
            .map { it.toCommentResult(command.myUserId, 0) }
            .sortedByDescending { it.id }

        return parentComment.toCommentDetail(reCommentResults)
    }

    @Transactional
    override fun create(command: CreateCommentCommand) {
        val query = command.toCreateCommentQuery()
        commentGateway.create(query)
    }

    @Transactional(readOnly = true)
    override fun qty(feedId: Long): Int {
        return commentGateway.qty(feedId)
    }

    @Transactional
    override fun remove(commentId: Long) {
        commentGateway.remove(commentId)
    }

    @Transactional
    override fun modify(command: ModifyCommentCommand): CommentResult {
        val query = command.toModifyCommentQuery()
        val modified = commentGateway.modify(query)
        return modified.toCommentResult()
    }
}
