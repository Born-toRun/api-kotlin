package kr.kro.btr.infrastructure.model

import kr.kro.btr.domain.constant.Bucket
import kr.kro.btr.support.TokenDetail
import org.springframework.web.multipart.MultipartFile

data class ModifyObjectStorageQuery(
    val targetFileId: Long,
    val cdnUri: String,
    val my: TokenDetail,
    val file: MultipartFile,
    val bucket: Bucket
)