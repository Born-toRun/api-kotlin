package kr.kro.btr.domain.port.model

import kr.kro.btr.domain.constant.Bucket
import kr.kro.btr.domain.model.FileUpload

data class UploadObjectStorageCommand(
    val myUserId: Long,
    val file: FileUpload,
    val bucket: Bucket
)
