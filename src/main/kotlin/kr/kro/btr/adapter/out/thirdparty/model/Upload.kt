package kr.kro.btr.adapter.out.thirdparty.model

import kr.kro.btr.domain.constant.Bucket
import kr.kro.btr.domain.model.FileUpload

data class Upload(
    val file: FileUpload,
    val myUserId: Long,
    val bucket: Bucket
)