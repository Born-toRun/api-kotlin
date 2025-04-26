package kr.kro.btr.adapter.`in`.web.proxy

import kr.kro.btr.core.converter.ObjectStorageConverter
import kr.kro.btr.domain.constant.Bucket
import kr.kro.btr.domain.port.ObjectStoragePort
import kr.kro.btr.domain.port.model.ObjectStorage
import kr.kro.btr.support.TokenDetail
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile

@Component
class ObjectStorageProxy(
    private val objectStorageConverter: ObjectStorageConverter,
    private val objectStoragePort: ObjectStoragePort
) {

    fun upload(my: TokenDetail, bucket: Bucket, file: MultipartFile): ObjectStorage {
        val command = objectStorageConverter.map(my.id, file, bucket)
        return objectStoragePort.upload(command)
    }

    fun remove(my: TokenDetail, bucket: Bucket, fileId: Long) {
        val command = objectStorageConverter.map(my, fileId, bucket)
        objectStoragePort.remove(command)
    }
}
