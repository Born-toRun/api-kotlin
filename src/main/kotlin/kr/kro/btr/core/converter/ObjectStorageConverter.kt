package kr.kro.btr.core.converter

import kr.kro.btr.adapter.`in`.web.payload.UploadFileResponse
import kr.kro.btr.adapter.out.thirdparty.model.Remove
import kr.kro.btr.adapter.out.thirdparty.model.RemoveAll
import kr.kro.btr.adapter.out.thirdparty.model.Upload
import kr.kro.btr.core.event.model.MinioRemoveAllEventModel
import kr.kro.btr.core.event.model.MinioRemoveEventModel
import kr.kro.btr.domain.constant.Bucket
import kr.kro.btr.domain.entity.ObjectStorageEntity
import kr.kro.btr.domain.port.model.ObjectStorage
import kr.kro.btr.domain.port.model.RemoveObjectStorageCommand
import kr.kro.btr.domain.port.model.UploadObjectStorageCommand
import kr.kro.btr.infrastructure.model.ModifyObjectStorageQuery
import kr.kro.btr.infrastructure.model.RemoveObjectStorageQuery
import kr.kro.btr.infrastructure.model.UploadObjectStorageQuery
import kr.kro.btr.support.TokenDetail
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import kotlin.Long

@Component
class ObjectStorageConverter {

    fun map(source: ObjectStorage): UploadFileResponse {
        return UploadFileResponse(
            fileId = source.id,
            fileUri = source.fileUri
        )
    }

    fun map(userId: Long, file: MultipartFile, bucket: Bucket): UploadObjectStorageCommand {
        return UploadObjectStorageCommand(
            myUserId = userId,
            file = file,
            bucket = bucket
        )
    }

    fun map(my: TokenDetail, fileId: Long, bucket: Bucket): RemoveObjectStorageCommand {
        return RemoveObjectStorageCommand(
            my = my,
            targetFileId = fileId,
            bucket = bucket
        )
    }

    fun map(source: UploadObjectStorageCommand): UploadObjectStorageQuery {
        return UploadObjectStorageQuery(
            myUserId = source.myUserId,
            file = source.file,
            bucket = source.bucket
        )
    }

    fun map(source: ObjectStorageEntity): ObjectStorage {
        return ObjectStorage(
            id = source.id,
            userId = source.userId,
            fileUri = source.fileUri,
            uploadAt = source.uploadAt
        )
    }

    fun map(source: RemoveObjectStorageCommand): RemoveObjectStorageQuery {
        return RemoveObjectStorageQuery(
            my = source.my,
            targetFileId = source.targetFileId,
            bucket = source.bucket
        )
    }

    fun map(source: UploadObjectStorageQuery): Upload {
        return Upload(
            file = source.file,
            myUserId = source.myUserId,
            bucket = source.bucket
        )
    }

    fun map(source: ModifyObjectStorageQuery): Upload {
        return Upload(
            file = source.file,
            myUserId = source.my.id,
            bucket = source.bucket
        )
    }

    fun map(source: MinioRemoveEventModel): Remove {
        return Remove(
            bucket = source.bucket,
            objectName = source.objectName
        )
    }

    fun map(source: MinioRemoveAllEventModel): RemoveAll {
        return RemoveAll(
            bucket = source.bucket,
            objectNames = source.objectNames
        )
    }
}
