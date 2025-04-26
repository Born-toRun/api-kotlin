package kr.kro.btr.infrastructure.model

import kr.kro.btr.domain.constant.Bucket
import org.springframework.web.multipart.MultipartFile

data class UploadObjectStorageQuery(
    val myUserId: Long,
    val file: MultipartFile,
    val bucket: Bucket
)