package kr.kro.btr.infrastructure

import kr.kro.btr.adapter.out.thirdparty.ObjectStorageClient
import kr.kro.btr.adapter.out.thirdparty.model.Remove
import kr.kro.btr.adapter.out.thirdparty.model.RemoveAll
import kr.kro.btr.adapter.out.thirdparty.model.Upload
import org.springframework.stereotype.Component

@Component
class MinioGateway(
    private val objectStorageClient: ObjectStorageClient
) {

    fun removeObject(remove: Remove) {
        objectStorageClient.remove(remove)
    }

    fun removeObjects(removeAll: RemoveAll) {
        objectStorageClient.removeAll(removeAll)
    }

    fun uploadObject(upload: Upload): String {
        return objectStorageClient.upload(upload)
    }
}
