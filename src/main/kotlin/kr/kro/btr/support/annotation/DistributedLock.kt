package kr.kro.btr.support.annotation

import java.util.concurrent.TimeUnit

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class DistributedLock(
    val key: String,
    val timeUnit: TimeUnit = TimeUnit.SECONDS,    // 락 시간 단위
    val waitTime: Long = 5L,      // 락 획득을 위해 waitTime(s) 만큼 대기
    val leaseTime: Long = 5L     // 락을 획득한 이후 leaseTime(s) 이 지나면 락을 해제
)
