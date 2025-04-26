package kr.kro.btr.domain.port

import kr.kro.btr.domain.port.model.ObjectStorage
import kr.kro.btr.domain.port.model.RemoveObjectStorageCommand
import kr.kro.btr.domain.port.model.UploadObjectStorageCommand

interface ObjectStoragePort {
    fun upload(command: UploadObjectStorageCommand): ObjectStorage
    fun remove(command: RemoveObjectStorageCommand)
}