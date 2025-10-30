package kr.kro.btr.infrastructure.model

import kr.kro.btr.domain.constant.Bucket
import kr.kro.btr.domain.model.FileUpload

data class UploadObjectStorageQuery(
    val myUserId: Long,
    val file: FileUpload,
    val bucket: Bucket
)