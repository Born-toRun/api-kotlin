package kr.kro.btr.adapter.out.thirdparty.model

import kr.kro.btr.domain.constant.Bucket

data class RemoveAll(
    val bucket: Bucket,
    val objectNames: List<String>
)