package kr.kro.btr.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding

@ConfigurationProperties("adapter.objectstorage.minio")
data class MinioProperties @ConstructorBinding constructor (
    val node: String,
    val accessKey: String,
    val secretKey: String,
    val cdnHost: String,
    val region: String? = null
)
