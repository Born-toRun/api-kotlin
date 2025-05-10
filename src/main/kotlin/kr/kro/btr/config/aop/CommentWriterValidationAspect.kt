package kr.kro.btr.config.aop

import kr.kro.btr.adapter.`in`.web.payload.ModifyCommentRequest
import kr.kro.btr.adapter.out.persistence.CommentRepository
import kr.kro.btr.support.TokenDetail
import kr.kro.btr.support.exception.ForBiddenException
import kr.kro.btr.support.exception.NotFoundException
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.annotation.Pointcut
import org.springframework.stereotype.Component

@Aspect
@Component
class CommentWriterValidationAspect(private val componentRepository: CommentRepository) {

    @Pointcut("execution(* kr.kro.btr.adapter.in.web.CommentController.modify(..))")
    fun onModifyUser() {}

    @Pointcut("execution(* kr.kro.btr.adapter.in.web.CommentController.remove(..))")
    fun onDeleteUser() {}

    @Before("onModifyUser() && args(my, commentId, request, ..)")
    fun validateModify(my: TokenDetail, commentId: Long, request: ModifyCommentRequest) {
        validate(my.id, commentId)
    }

    @Before("onDeleteUser() && args(my, commentId, ..)")
    fun validateDelete(my: TokenDetail, commentId: Long) {
        validate(my.id, commentId)
    }

    private fun validate(userId: Long, commentId: Long) {
        if (!isValid(userId, commentId)) {
            throw ForBiddenException("잘못된 접근입니다.")
        }
    }

    private fun isValid(userId: Long, commentId: Long): Boolean {
        val comment = componentRepository.findByIdOrNull(commentId) ?: throw NotFoundException("댓글를 찾을 수 없습니다.")
        return comment.userId == userId
    }
}
