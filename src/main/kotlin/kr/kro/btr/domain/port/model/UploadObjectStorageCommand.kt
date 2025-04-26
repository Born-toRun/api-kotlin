package kr.kro.btr.domain.port.model

import kr.kro.btr.domain.constant.Bucket
import org.springframework.web.multipart.MultipartFile

data class UploadObjectStorageCommand(
    val myUserId: Long,
    val file: MultipartFile,
    val bucket: Bucket
)
