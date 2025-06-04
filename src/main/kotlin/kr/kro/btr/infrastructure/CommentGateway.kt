package kr.kro.btr.infrastructure

import kr.kro.btr.adapter.out.persistence.CommentRepository
import kr.kro.btr.base.extension.toCommentEntity
import kr.kro.btr.domain.entity.CommentEntity
import kr.kro.btr.infrastructure.model.CreateCommentQuery
import kr.kro.btr.infrastructure.model.ModifyCommentQuery
import kr.kro.btr.support.exception.NotFoundException
import org.springframework.stereotype.Component

@Component
class CommentGateway(
    private val commentRepository: CommentRepository
) {

    fun searchAll(feedId: Long): List<CommentEntity> {
        return commentRepository.findAllByFeedId(feedId)
    }

    fun searchAll(feedIds: List<Long>): List<CommentEntity> {
        return commentRepository.findAllByFeedIdIn(feedIds)
    }

    fun create(query: CreateCommentQuery) {
        if (query.parentCommentId != null) {
            commentRepository.findByIdOrNull(query.parentCommentId) ?: throw NotFoundException("부모 댓글을 찾을 수 없습니다.")
        }
        val commentEntity = query.toCommentEntity()
        commentRepository.save(commentEntity)
    }

    fun qty(feedId: Long): Int {
        return commentRepository.countByFeedId(feedId)
    }

    fun search(commentId: Long): CommentEntity {
        return commentRepository.findByIdOrNull(commentId) ?: throw NotFoundException("댓글을 찾을 수 없습니다.")
    }

    fun searchReComments(commentId: Long): List<CommentEntity> {
        return commentRepository.findAllByParentId(commentId)
    }

    fun remove(commentId: Long) {
        commentRepository.deleteById(commentId)
    }

    fun removeAll(commentIds: List<Long>) {
        commentRepository.deleteAllById(commentIds)
    }

    fun removeAll(userId: Long) {
        val commentIds = commentRepository.findAllByUserId(userId).map { it.id }
        commentRepository.deleteAllById(commentIds)
    }

    fun modify(query: ModifyCommentQuery): CommentEntity {
        val comment = search(query.commentId)
        comment.contents = query.contents

        return commentRepository.save(comment)
    }
}
