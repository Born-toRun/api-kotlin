package kr.kro.btr.adapter.`in`.web.proxy

import kr.kro.btr.base.extension.toRemoveObjectStorageCommand
import kr.kro.btr.base.extension.toUploadObjectStorageCommand
import kr.kro.btr.domain.constant.Bucket
import kr.kro.btr.domain.port.ObjectStoragePort
import kr.kro.btr.domain.port.model.result.ObjectStorageResult
import kr.kro.btr.support.TokenDetail
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile

@Component
class ObjectStorageProxy(
    private val objectStoragePort: ObjectStoragePort
) {

    fun upload(my: TokenDetail, bucket: Bucket, file: MultipartFile): ObjectStorageResult {
        val command = my.toUploadObjectStorageCommand(file, bucket)
        return objectStoragePort.upload(command)
    }

    fun remove(my: TokenDetail, bucket: Bucket, fileId: Long) {
        val command = my.toRemoveObjectStorageCommand(fileId, bucket)
        objectStoragePort.remove(command)
    }
}
