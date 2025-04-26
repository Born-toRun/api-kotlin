package kr.kro.btr.core.service

import kr.kro.btr.core.converter.ObjectStorageConverter
import kr.kro.btr.domain.port.ObjectStoragePort
import kr.kro.btr.domain.port.model.ObjectStorage
import kr.kro.btr.domain.port.model.RemoveObjectStorageCommand
import kr.kro.btr.domain.port.model.UploadObjectStorageCommand
import kr.kro.btr.infrastructure.ObjectStorageGateway
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ObjectStorageService(
    private val objectStorageConverter: ObjectStorageConverter,
    private val objectStorageGateway: ObjectStorageGateway
) : ObjectStoragePort {

    @Transactional
    override fun upload(command: UploadObjectStorageCommand): ObjectStorage {
        val query = objectStorageConverter.map(command)
        val uploaded = objectStorageGateway.upload(query)
        return objectStorageConverter.map(uploaded)
    }

    @Transactional
    override fun remove(command: RemoveObjectStorageCommand) {
        val query = objectStorageConverter.map(command)
        objectStorageGateway.remove(query)
    }
}