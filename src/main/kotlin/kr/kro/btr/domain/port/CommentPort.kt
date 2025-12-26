package kr.kro.btr.domain.port

import kr.kro.btr.domain.port.model.CreateCommentCommand
import kr.kro.btr.domain.port.model.DetailCommentCommand
import kr.kro.btr.domain.port.model.ModifyCommentCommand
import kr.kro.btr.domain.port.model.SearchAllCommentCommand
import kr.kro.btr.domain.port.model.result.CommentDetailResult
import kr.kro.btr.domain.port.model.result.CommentResult

interface CommentPort {
    fun searchAll(command: SearchAllCommentCommand): List<CommentResult>
    fun detail(command: DetailCommentCommand): CommentDetailResult
    fun create(command: CreateCommentCommand)
    fun qty(feedId: Long): Int
    fun remove(commentId: Long)
    fun modify(command: ModifyCommentCommand): CommentResult
}
