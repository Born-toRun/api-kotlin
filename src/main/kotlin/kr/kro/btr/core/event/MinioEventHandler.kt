package kr.kro.btr.core.event

import io.github.oshai.kotlinlogging.KotlinLogging
import kr.kro.btr.base.extension.toRemove
import kr.kro.btr.base.extension.toRemoveAll
import kr.kro.btr.infrastructure.MinioGateway
import kr.kro.btr.infrastructure.event.MinioRemoveAllEventModel
import kr.kro.btr.infrastructure.event.MinioRemoveEventModel
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class MinioEventHandler(
    private val minioGateway: MinioGateway,
) {
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    fun onObjectStorageRemoved(event: MinioRemoveEventModel) {
        log.info { "${event.bucket.bucketName} Bucket에서 ${event.objectName} 파일을 제거합니다." }
        minioGateway.removeObject(event.toRemove())
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    fun onObjectStorageRemoved(event: MinioRemoveAllEventModel) {
        log.info { "${event.bucket.bucketName} Bucket에서 ${event.objectNames} 파일들을 제거합니다." }
        minioGateway.removeObjects(event.toRemoveAll())
    }

    companion object {
        private val log = KotlinLogging.logger {}
    }
}
