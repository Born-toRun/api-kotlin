package kr.kro.btr.core.event.model

import kr.kro.btr.domain.constant.Bucket

data class MinioRemoveEventModel(val bucket: Bucket, val objectName: String)