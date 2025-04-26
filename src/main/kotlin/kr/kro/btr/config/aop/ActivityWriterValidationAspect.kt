package kr.kro.btr.config.aop

import kr.kro.btr.adapter.`in`.web.payload.ModifyActivityRequest
import kr.kro.btr.adapter.out.persistence.ActivityRepository
import kr.kro.btr.support.TokenDetail
import kr.kro.btr.support.exception.ForBiddenException
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.annotation.Pointcut
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Aspect
@Component
class ActivityWriterValidationAspect(private val activityRepository: ActivityRepository) {

    @Pointcut("execution(* kr.kro.btr.adapter.in.web.ActivityController.open(..))")
    fun onOpenUser() {}

    @Pointcut("execution(* kr.kro.btr.adapter.in.web.ActivityController.modify(..))")
    fun onModifyUser() {}

    @Pointcut("execution(* kr.kro.btr.adapter.in.web.ActivityController.remove(..))")
    fun onDeleteUser() {}

    @Before("onOpenUser() && args(my, activityId, ..)")
    fun validateOpen(my: TokenDetail, activityId: Long) {
        validate(my.id, activityId)
    }

    @Before("onModifyUser() && args(my, activityId, request, ..)")
    fun validateModify(my: TokenDetail, activityId: Long, request: ModifyActivityRequest) {
        validate(my.id, activityId)
    }

    @Before("onDeleteUser() && args(my, activityId, ..)")
    fun validateDelete(my: TokenDetail, activityId: Long) {
        validate(my.id, activityId)
    }

    private fun validate(userId: Long, activityId: Long) {
        if (!isValid(userId, activityId)) {
            throw ForBiddenException("잘못된 접근입니다.")
        }
    }

    private fun isValid(userId: Long, activityId: Long): Boolean {
        val activity = activityRepository.findByIdOrNull(activityId) ?: throw NoSuchElementException("존재하지 않는 활동입니다.")

        return activity.userId == userId
    }
}
