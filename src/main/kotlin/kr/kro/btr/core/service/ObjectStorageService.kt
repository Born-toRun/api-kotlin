package kr.kro.btr.core.service

import kr.kro.btr.base.extension.toObjectStorage
import kr.kro.btr.base.extension.toRemoveObjectStorageQuery
import kr.kro.btr.base.extension.toUploadObjectStorageQuery
import kr.kro.btr.domain.port.ObjectStoragePort
import kr.kro.btr.domain.port.model.RemoveObjectStorageCommand
import kr.kro.btr.domain.port.model.UploadObjectStorageCommand
import kr.kro.btr.domain.port.model.result.ObjectStorageResult
import kr.kro.btr.infrastructure.ObjectStorageGateway
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ObjectStorageService(
    private val objectStorageGateway: ObjectStorageGateway
) : ObjectStoragePort {

    @Transactional
    override fun upload(command: UploadObjectStorageCommand): ObjectStorageResult {
        val query = command.toUploadObjectStorageQuery()
        val uploaded = objectStorageGateway.upload(query)
        return uploaded.toObjectStorage()
    }

    @Transactional
    override fun remove(command: RemoveObjectStorageCommand) {
        val query = command.toRemoveObjectStorageQuery()
        objectStorageGateway.remove(query)
    }
}
