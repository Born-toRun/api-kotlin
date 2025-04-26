package kr.kro.btr.infrastructure.model

import kr.kro.btr.domain.constant.Bucket
import kr.kro.btr.support.TokenDetail

data class RemoveObjectStorageQuery(
    val my: TokenDetail,
    val targetFileId: Long,
    val bucket: Bucket
)