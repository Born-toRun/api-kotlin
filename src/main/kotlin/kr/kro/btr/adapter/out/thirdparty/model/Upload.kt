package kr.kro.btr.adapter.out.thirdparty.model

import kr.kro.btr.domain.constant.Bucket
import org.springframework.web.multipart.MultipartFile

data class Upload(
    val file: MultipartFile,
    val myUserId: Long,
    val bucket: Bucket
)