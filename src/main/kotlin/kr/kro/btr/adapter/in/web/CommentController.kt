package kr.kro.btr.adapter.`in`.web

import jakarta.validation.Valid
import kr.kro.btr.adapter.`in`.web.payload.CreateCommentRequest
import kr.kro.btr.adapter.`in`.web.payload.DetailCommentResponse
import kr.kro.btr.adapter.`in`.web.payload.ModifyCommentRequest
import kr.kro.btr.adapter.`in`.web.payload.ModifyCommentResponse
import kr.kro.btr.adapter.`in`.web.payload.QtyCommentResponse
import kr.kro.btr.adapter.`in`.web.payload.SearchCommentsResponse
import kr.kro.btr.adapter.`in`.web.proxy.CommentProxy
import kr.kro.btr.base.extension.toModifyCommentResponse
import kr.kro.btr.base.extension.toSearchCommentDetailResponse
import kr.kro.btr.base.extension.toSearchCommentResponse
import kr.kro.btr.domain.port.model.result.CommentDetailResult
import kr.kro.btr.domain.port.model.result.CommentResult
import kr.kro.btr.support.TokenDetail
import kr.kro.btr.support.annotation.AuthUser
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/comments")
class CommentController(
    private val commentProxy: CommentProxy
) {

    @GetMapping("/{feedId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun searchAll(
        @AuthUser my: TokenDetail,
        @PathVariable feedId: Long
    ): ResponseEntity<SearchCommentsResponse> {
        val commentResults: List<CommentResult> = commentProxy.searchAll(feedId, my)
        val response = commentResults.toSearchCommentResponse()
        return ResponseEntity.ok(response)
    }

    @GetMapping("/detail/{commentId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun detail(
        @AuthUser my: TokenDetail,
        @PathVariable commentId: Long
    ): ResponseEntity<DetailCommentResponse> {
        val commentDetailResult: CommentDetailResult = commentProxy.detail(commentId, my)
        val response = commentDetailResult.toSearchCommentDetailResponse()
        return ResponseEntity.ok(response)
    }

    @PostMapping("/{feedId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun create(
        @AuthUser my: TokenDetail,
        @PathVariable feedId: Long,
        @RequestBody @Valid request: CreateCommentRequest
    ): ResponseEntity<Void> {
        commentProxy.create(my, feedId, request)
        return ResponseEntity(CREATED)
    }

    @DeleteMapping("/{commentId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun remove(@AuthUser my: TokenDetail, @PathVariable commentId: Long): ResponseEntity<Void> {
        commentProxy.remove(commentId)
        return ResponseEntity.ok().build()
    }

    @PutMapping("/{commentId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun modify(
        @AuthUser my: TokenDetail,
        @PathVariable commentId: Long,
        @RequestBody @Valid request: ModifyCommentRequest
    ): ResponseEntity<ModifyCommentResponse> {
        val result: CommentResult = commentProxy.modify(commentId, request)
        val response = result.toModifyCommentResponse()
        return ResponseEntity.ok(response)
    }

    @GetMapping("/qty/{feedId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun qty(@PathVariable feedId: Long): ResponseEntity<QtyCommentResponse> {
        val qty: Int = commentProxy.qty(feedId)
        return ResponseEntity.ok(QtyCommentResponse(qty))
    }
}
