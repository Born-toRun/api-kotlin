package kr.kro.btr.core.event

import io.github.oshai.kotlinlogging.KotlinLogging
import kr.kro.btr.core.converter.ObjectStorageConverter
import kr.kro.btr.core.event.model.MinioRemoveAllEventModel
import kr.kro.btr.core.event.model.MinioRemoveEventModel
import kr.kro.btr.infrastructure.MinioGateway
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class MinioEventHandler(
    private val minioGateway: MinioGateway,
    private val objectStorageConverter: ObjectStorageConverter
) {
    companion object {
        private val log = KotlinLogging.logger {}
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    fun onObjectStorageRemoved(event: MinioRemoveEventModel) {
        log.info { "${event.bucket.bucketName} Bucket에서 ${event.objectName} 파일을 제거합니다." }
        minioGateway.removeObject(objectStorageConverter.map(event))
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    fun onObjectStorageRemoved(event: MinioRemoveAllEventModel) {
        log.info { "${event.bucket.bucketName} Bucket에서 ${event.objectNames} 파일들을 제거합니다." }
        minioGateway.removeObjects(objectStorageConverter.map(event))
    }
}
