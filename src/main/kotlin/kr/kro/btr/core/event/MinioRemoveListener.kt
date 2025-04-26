package kr.kro.btr.core.event

import jakarta.persistence.PreRemove
import kr.kro.btr.core.event.model.MinioRemoveEventModel
import kr.kro.btr.domain.constant.Bucket
import kr.kro.btr.domain.entity.ObjectStorageEntity
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

@Component
class MinioRemoveListener(
    private val eventPublisher: ApplicationEventPublisher
) {

    @PreRemove
    fun onPreRemove(objectStorageEntity: ObjectStorageEntity) {
        val fileUri = objectStorageEntity.fileUri
        if (fileUri != null) {
            val bucketName = Bucket.valueOf(objectStorageEntity.bucketName!!.uppercase())
            val fileName = fileUri.substring(fileUri.lastIndexOf("/") + 1)
            eventPublisher.publishEvent(
                MinioRemoveEventModel(bucketName, fileName)
            )
        }
    }
}
