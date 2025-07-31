package kr.kro.btr.domain.port

import kr.kro.btr.domain.port.model.RemoveObjectStorageCommand
import kr.kro.btr.domain.port.model.UploadObjectStorageCommand
import kr.kro.btr.domain.port.model.result.ObjectStorageResult

interface ObjectStoragePort {
    fun upload(command: UploadObjectStorageCommand): ObjectStorageResult
    fun remove(command: RemoveObjectStorageCommand)
}
