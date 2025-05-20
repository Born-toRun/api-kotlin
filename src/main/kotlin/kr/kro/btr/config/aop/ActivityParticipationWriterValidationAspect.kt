package kr.kro.btr.config.aop

import kr.kro.btr.adapter.out.persistence.ActivityParticipationRepository
import kr.kro.btr.base.extension.findByIdOrThrow
import kr.kro.btr.support.TokenDetail
import kr.kro.btr.support.exception.ForBiddenException
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.annotation.Pointcut
import org.springframework.stereotype.Component

@Aspect
@Component
class ActivityParticipationWriterValidationAspect(
    private val activityParticipationRepository: ActivityParticipationRepository
) {

    @Pointcut("execution(* kr.kro.btr.adapter.in.web.ActivityController.participateCancel(..))")
    fun onCancelUser() {}

    @Before("onCancelUser() && args(my, participationId, ..)")
    fun validateCancel(my: TokenDetail, participationId: Long) {
        validate(my.id, participationId)
    }

    private fun validate(userId: Long, participationId: Long) {
        if (!isValid(userId, participationId)) {
            throw ForBiddenException("잘못된 접근입니다.")
        }
    }

    private fun isValid(userId: Long, participationId: Long): Boolean {
        val participation = activityParticipationRepository.findByIdOrThrow(participationId)

        return participation.userId == userId
    }
}
