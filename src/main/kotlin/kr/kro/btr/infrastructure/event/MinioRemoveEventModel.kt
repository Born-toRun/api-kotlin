package kr.kro.btr.infrastructure.event

import kr.kro.btr.domain.constant.Bucket

data class MinioRemoveEventModel(val bucket: Bucket, val objectName: String)
