package kr.kro.btr.config.aop.joinPoint

import org.aspectj.lang.ProceedingJoinPoint
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Component
class JoinPointTransaction {

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun proceed(joinPoint: ProceedingJoinPoint): Any {
        return joinPoint.proceed()
    }
}
