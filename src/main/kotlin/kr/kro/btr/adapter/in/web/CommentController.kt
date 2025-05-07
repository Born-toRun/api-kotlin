package kr.kro.btr.adapter.`in`.web

import jakarta.validation.Valid
import kr.kro.btr.adapter.`in`.web.payload.CreateCommentRequest
import kr.kro.btr.adapter.`in`.web.payload.ModifyCommentRequest
import kr.kro.btr.adapter.`in`.web.payload.ModifyCommentResponse
import kr.kro.btr.adapter.`in`.web.payload.QtyCommentResponse
import kr.kro.btr.adapter.`in`.web.payload.SearchCommentDetailResponse
import kr.kro.btr.adapter.`in`.web.payload.SearchCommentResponse
import kr.kro.btr.adapter.`in`.web.proxy.CommentProxy
import kr.kro.btr.core.converter.CommentConverter
import kr.kro.btr.domain.port.model.CommentDetail
import kr.kro.btr.domain.port.model.CommentResult
import kr.kro.btr.support.TokenDetail
import kr.kro.btr.support.annotation.AuthUser
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/comments")
class CommentController(
    private val commentConverter: CommentConverter,
    private val commentProxy: CommentProxy
) {

    @GetMapping("/{feedId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun searchAll(
        @AuthUser my: TokenDetail,
        @PathVariable feedId: Long
    ): ResponseEntity<SearchCommentResponse> {
        val commentResults: List<CommentResult> = commentProxy.searchAll(feedId, my)
        val response = commentConverter.mapToSearchCommentResponse(commentResults)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/detail/{commentId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun detail(
        @AuthUser my: TokenDetail,
        @PathVariable commentId: Long
    ): ResponseEntity<SearchCommentDetailResponse> {
        val commentDetail: CommentDetail = commentProxy.detail(commentId, my)
        val response = commentConverter.map(commentDetail)
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
        val response = commentConverter.map(result)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/qty/{feedId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun qty(@PathVariable feedId: Long): ResponseEntity<QtyCommentResponse> {
        val qty: Int = commentProxy.qty(feedId)
        return ResponseEntity.ok(QtyCommentResponse(qty))
    }
}
