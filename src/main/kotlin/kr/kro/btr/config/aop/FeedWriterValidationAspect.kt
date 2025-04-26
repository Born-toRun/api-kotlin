package kr.kro.btr.config.aop

import kr.kro.btr.adapter.`in`.web.payload.ModifyFeedRequest
import kr.kro.btr.adapter.out.persistence.FeedRepository
import kr.kro.btr.support.TokenDetail
import kr.kro.btr.support.exception.ForBiddenException
import kr.kro.btr.support.exception.NotFoundException
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.annotation.Pointcut
import org.springframework.stereotype.Component

@Aspect
@Component
class FeedWriterValidationAspect(
    private val feedRepository: FeedRepository
) {

    @Pointcut("execution(* kr.kro.btr.adapter.in.web.FeedController.modify(..))")
    fun onModifyUser() {}

    @Pointcut("execution(* kr.kro.btr.adapter.in.web.FeedController.remove(..))")
    fun onDeleteUser() {}

    @Before("onModifyUser() && args(my, feedId, request, ..)")
    fun validateModify(my: TokenDetail, feedId: Long, request: ModifyFeedRequest) {
        validate(my.id, feedId)
    }

    @Before("onDeleteUser() && args(my, feedId, ..)")
    fun validateDelete(my: TokenDetail, feedId: Long) {
        validate(my.id, feedId)
    }

    private fun validate(userId: Long, feedId: Long) {
        if (!isValid(userId, feedId)) {
            throw ForBiddenException("잘못된 접근입니다.")
        }
    }

    private fun isValid(userId: Long, feedId: Long): Boolean {
        val feed = feedRepository.findByIdOrNull(feedId) ?: throw NotFoundException("피드를 찾을 수 없습니다.")
        return feed.userId == userId
    }
}

