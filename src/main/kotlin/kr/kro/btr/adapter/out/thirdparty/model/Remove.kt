package kr.kro.btr.adapter.out.thirdparty.model

import kr.kro.btr.domain.constant.Bucket

data class Remove(
    val bucket: Bucket,
    val objectName: String
)