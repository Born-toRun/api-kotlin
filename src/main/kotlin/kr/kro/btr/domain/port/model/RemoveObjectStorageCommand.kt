package kr.kro.btr.domain.port.model

import kr.kro.btr.domain.constant.Bucket
import kr.kro.btr.support.TokenDetail


data class RemoveObjectStorageCommand(
    val my: TokenDetail,
    val targetFileId: Long,
    val bucket: Bucket
)
