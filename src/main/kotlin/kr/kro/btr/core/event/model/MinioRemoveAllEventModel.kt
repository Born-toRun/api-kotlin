package kr.kro.btr.core.event.model

import kr.kro.btr.domain.constant.Bucket

data class MinioRemoveAllEventModel(val bucket: Bucket, val objectNames: List<String>)