package kr.kro.btr.adapter.`in`.web.proxy

import kr.kro.btr.adapter.`in`.web.payload.CreateCommentRequest
import kr.kro.btr.adapter.`in`.web.payload.ModifyCommentRequest
import kr.kro.btr.base.extension.toCreateCommentCommand
import kr.kro.btr.base.extension.toModifyCommentCommand
import kr.kro.btr.domain.port.CommentPort
import kr.kro.btr.domain.port.model.CommentDetail
import kr.kro.btr.domain.port.model.CommentResult
import kr.kro.btr.domain.port.model.DetailCommentCommand
import kr.kro.btr.domain.port.model.SearchAllCommentCommand
import kr.kro.btr.support.TokenDetail
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component

@Component
@CacheConfig(cacheNames = ["comment"])
class CommentProxy(
    private val commentPort: CommentPort
) {

    @Cacheable(key = "'searchAll: ' + #feedId + #my.id")
    fun searchAll(feedId: Long, my: TokenDetail): List<CommentResult> {
        val command = SearchAllCommentCommand(feedId, my.id)
        return commentPort.searchAll(command)
    }

    @Cacheable(key = "'detail: ' + #commentId + #my.id")
    fun detail(commentId: Long, my: TokenDetail): CommentDetail {
        val command = DetailCommentCommand(commentId, my.id)
        return commentPort.detail(command)
    }

    @CacheEvict(allEntries = true, cacheNames = ["comment", "feed"])
    fun create(my: TokenDetail, feedId: Long, request: CreateCommentRequest) {
        val command = request.toCreateCommentCommand(my.id, feedId)
        commentPort.create(command)
    }

    @Cacheable(key = "'qty: ' + #feedId")
    fun qty(feedId: Long): Int {
        return commentPort.qty(feedId)
    }

    @CacheEvict(allEntries = true, cacheNames = ["comment", "feed"])
    fun remove(commentId: Long) {
        commentPort.remove(commentId)
    }

    @CacheEvict(allEntries = true)
    fun modify(commentId: Long, request: ModifyCommentRequest): CommentResult {
        val command = request.toModifyCommentCommand(commentId)
        return commentPort.modify(command)
    }
}
