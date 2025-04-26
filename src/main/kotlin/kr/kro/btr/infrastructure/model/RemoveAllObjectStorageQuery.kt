package kr.kro.btr.infrastructure.model

import kr.kro.btr.domain.constant.Bucket
import kr.kro.btr.support.TokenDetail

data class RemoveAllObjectStorageQuery(
    val my: TokenDetail,
    val targetFileIds: MutableList<Long>,
    val bucket: Bucket
)