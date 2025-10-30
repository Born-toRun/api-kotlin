package kr.kro.btr.infrastructure.model

import kr.kro.btr.domain.constant.Bucket
import kr.kro.btr.domain.model.FileUpload
import kr.kro.btr.support.TokenDetail

data class ModifyObjectStorageQuery(
    val targetFileId: Long,
    val cdnUri: String,
    val my: TokenDetail,
    val file: FileUpload,
    val bucket: Bucket
)