package kr.kro.btr.config

import io.minio.MinioClient
import kr.kro.btr.config.properties.MinioProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(MinioProperties::class)
class MinioClientConfig(private val minioProperties: MinioProperties) {

    @Bean
    fun minioClient(): MinioClient {
        return MinioClient.builder()
            .endpoint(minioProperties.node)
            .credentials(minioProperties.accessKey, minioProperties.secretKey)
            .build()
    }
}