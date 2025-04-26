package kr.kro.btr.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("adapter.objectstorage.minio")
data class MinioProperties (
    val node: String,
    val accessKey: String,
    val secretKey: String,
    val cdnHost: String
)