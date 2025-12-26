package kr.kro.btr.infrastructure

import kr.kro.btr.adapter.out.persistence.ObjectStorageRepository
import kr.kro.btr.base.extension.findByIdOrThrow
import kr.kro.btr.base.extension.toUpload
import kr.kro.btr.config.properties.MinioProperties
import kr.kro.btr.domain.entity.ObjectStorageEntity
import kr.kro.btr.infrastructure.event.MinioRemoveEventModel
import kr.kro.btr.infrastructure.model.RemoveObjectStorageQuery
import kr.kro.btr.infrastructure.model.UploadObjectStorageQuery
import kr.kro.btr.support.exception.InvalidException
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

@Component
class ObjectStorageGateway(
    private val minioProperties: MinioProperties,
    private val objectStorageRepository: ObjectStorageRepository,
    private val minioGateway: MinioGateway,
    private val eventPublisher: ApplicationEventPublisher
) {

    fun upload(query: UploadObjectStorageQuery): ObjectStorageEntity {
        val uploadedFileName = minioGateway.uploadObject(query.toUpload())

        val cdnUri = "${minioProperties.cdnHost}/${query.bucket.bucketName}/$uploadedFileName"

        return objectStorageRepository.save(
            ObjectStorageEntity(
                fileUri = cdnUri,
                userId = query.myUserId,
                bucketName = query.bucket.name
            )
        )
    }

    fun search(id: Long): ObjectStorageEntity {
        return objectStorageRepository.findByIdOrThrow(id)
    }

    fun remove(query: RemoveObjectStorageQuery) {
        if (query.targetFileId == 0L) return

        val objectStorage = search(query.targetFileId)

        if (!query.my.isAdmin) {
            if (query.my.id != objectStorage.userId) {
                throw InvalidException("본인이 올린 파일만 제거할 수 있습니다.")
            }
        }

        objectStorageRepository.deleteById(objectStorage.id)

        eventPublisher.publishEvent(
            MinioRemoveEventModel(query.bucket, objectStorage.fileUri!!.substringAfterLast("/"))
        )
    }
}
