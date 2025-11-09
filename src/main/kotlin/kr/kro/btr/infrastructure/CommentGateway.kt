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
        return commentRepository.findAllByFeedIdOrderByIdDesc(feedId)
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

    fun searchWithReplies(commentId: Long): CommentEntity {
        return commentRepository.findByIdWithReplies(commentId) ?: throw NotFoundException("댓글을 찾을 수 없습니다.")
    }

    fun searchReComments(commentId: Long): List<CommentEntity> {
        return commentRepository.findAllByParentId(commentId)
    }

    fun remove(commentId: Long) {
        commentRepository.deleteById(commentId)
    }

    fun modify(query: ModifyCommentQuery): CommentEntity {
        val comment = search(query.commentId)
        comment.contents = query.contents

        return commentRepository.save(comment)
    }

    fun getReplyCountMap(parentIds: List<Long>): Map<Long, Int> {
        if (parentIds.isEmpty()) return emptyMap()
        return commentRepository.countGroupByParentId(parentIds)
            .associate {
                it.getParentId() to it.getCount().toInt()
            }
    }
}
