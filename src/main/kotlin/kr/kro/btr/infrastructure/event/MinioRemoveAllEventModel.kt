package kr.kro.btr.infrastructure.event

import kr.kro.btr.domain.constant.Bucket

data class MinioRemoveAllEventModel(val bucket: Bucket, val objectNames: List<String>)
