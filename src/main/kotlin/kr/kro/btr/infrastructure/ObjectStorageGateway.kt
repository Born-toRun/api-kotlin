package kr.kro.btr.infrastructure

import kr.kro.btr.adapter.out.persistence.ObjectStorageRepository
import kr.kro.btr.base.extension.findByIdOrThrow
import kr.kro.btr.config.properties.MinioProperties
import kr.kro.btr.core.converter.ObjectStorageConverter
import kr.kro.btr.core.event.model.MinioRemoveAllEventModel
import kr.kro.btr.core.event.model.MinioRemoveEventModel
import kr.kro.btr.domain.entity.ObjectStorageEntity
import kr.kro.btr.infrastructure.model.ModifyObjectStorageQuery
import kr.kro.btr.infrastructure.model.RemoveAllObjectStorageQuery
import kr.kro.btr.infrastructure.model.RemoveObjectStorageQuery
import kr.kro.btr.infrastructure.model.UploadObjectStorageQuery
import kr.kro.btr.support.exception.InvalidException
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

@Component
class ObjectStorageGateway(
    private val objectStorageConverter: ObjectStorageConverter,
    private val minioProperties: MinioProperties,
    private val objectStorageRepository: ObjectStorageRepository,
    private val minioGateway: MinioGateway,
    private val eventPublisher: ApplicationEventPublisher
) {

    fun upload(query: UploadObjectStorageQuery): ObjectStorageEntity {
        val uploadedFileName = minioGateway.uploadObject(objectStorageConverter.map(query))

        val cdnUri = "${minioProperties.cdnHost}/${query.bucket.name}/$uploadedFileName"

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

    fun searchAll(fileIds: List<Long>): List<ObjectStorageEntity> {
        return objectStorageRepository.findAllById(fileIds)
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

    fun removeAll(query: RemoveAllObjectStorageQuery) {
        if (query.targetFileIds.isEmpty()) return

        val objectStorages = objectStorageRepository.findAllById(query.targetFileIds)

        if (!query.my.isAdmin && objectStorages.none { it.userId == query.my.id }) {
            throw InvalidException("본인이 올린 파일만 제거할 수 있습니다.")
        }

        objectStorageRepository.deleteAllById(objectStorages.map { it.id })

        eventPublisher.publishEvent(
            MinioRemoveAllEventModel(query.bucket, objectStorages.map {
                it.fileUri.substringAfterLast("/")
            })
        )
    }

    fun modify(query: ModifyObjectStorageQuery): String {
        val objectStorage = search(query.targetFileId)

        if (!query.my.isAdmin) {
            if (query.my.id != objectStorage.userId) {
                throw InvalidException("본인이 올린 파일만 수정할 수 있습니다.")
            }
        }

        val uploadedFileName = minioGateway.uploadObject(objectStorageConverter.map(query))
        val targetCdnUri = objectStorage.fileUri
        val cdnUri = "${minioProperties.cdnHost}/${query.bucket}/$uploadedFileName"

        objectStorage.fileUri = cdnUri
        objectStorageRepository.save(objectStorage)

        eventPublisher.publishEvent(
            MinioRemoveEventModel(query.bucket, targetCdnUri.substringAfterLast("/"))
        )

        return cdnUri
    }
}
