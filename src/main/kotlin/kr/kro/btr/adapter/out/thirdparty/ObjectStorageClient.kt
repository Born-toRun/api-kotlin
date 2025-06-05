package kr.kro.btr.adapter.out.thirdparty

import io.github.oshai.kotlinlogging.KotlinLogging
import io.minio.*
import io.minio.errors.*
import io.minio.messages.DeleteObject
import kr.kro.btr.adapter.out.thirdparty.model.Remove
import kr.kro.btr.adapter.out.thirdparty.model.RemoveAll
import kr.kro.btr.adapter.out.thirdparty.model.Upload
import kr.kro.btr.support.exception.ClientUnknownException
import kr.kro.btr.support.exception.CryptoException
import kr.kro.btr.support.exception.InvalidException
import kr.kro.btr.support.exception.NetworkException
import org.springframework.stereotype.Component
import java.io.IOException
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.util.*

@Component
class ObjectStorageClient(
    private val minioClient: MinioClient
) {
    private fun getFileExtension(fileName: String?): String {
        val name = fileName ?: throw InvalidException("확장자가 없는 파일은 업로드 할 수 없습니다.")
        val dotIndex = name.lastIndexOf('.')
        if (dotIndex > 0 && dotIndex < name.length - 1) {
            return name.substring(dotIndex)
        }
        throw InvalidException("확장자가 없는 파일은 업로드 할 수 없습니다.")
    }

    fun upload(resource: Upload): String {
        try {
            val originalFilename = resource.file.originalFilename
            val extension = getFileExtension(originalFilename)
            val uploadedFileName = UUID.randomUUID().toString() + extension
            log.info { "$resource.bucket 에 $uploadedFileName 을 저장합니다." }

            minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket(resource.bucket.bucketName)
                    .`object`(uploadedFileName)
                    .contentType("image/${extension.substring(1)}")
                    .stream(resource.file.inputStream, resource.file.size, -1)
                    .build()
            )

            return uploadedFileName
        } catch (e: ErrorResponseException) {
            log.error { "MinIO 서버 오류: $e" }
            throw NetworkException("파일 업로드에 실패하였습니다.")
        } catch (e: ServerException) {
            log.error { "MinIO 서버 오류: $e" }
            throw NetworkException("파일 업로드에 실패하였습니다.")
        } catch (e: InsufficientDataException) {
            log.error { "파일의 데이터가 전송 도중 끊겼습니다: $e" }
            throw NetworkException("파일 업로드에 실패하였습니다.")
        } catch (e: InternalException) {
            log.error { "MinioClient 오류: $e" }
            throw NetworkException("파일 업로드에 실패하였습니다.")
        } catch (e: InvalidKeyException) {
            log.error { "암호화 키가 잘못되었습니다: $e" }
            throw CryptoException("파일 업로드에 실패하였습니다.")
        } catch (e: InvalidResponseException) {
            log.error { "알 수 없는 오류: $e" }
            throw ClientUnknownException("파일 업로드에 실패하였습니다.")
        } catch (e: IOException) {
            log.error { "파일 입출력 오류: $e" }
            throw NetworkException("파일 업로드에 실패하였습니다.")
        } catch (e: NoSuchAlgorithmException) {
            log.error { "암호화나 해시 등의 알고리즘이 지원되지 않습니다: $e" }
            throw CryptoException("파일 업로드에 실패하였습니다.")
        } catch (e: XmlParserException) {
            log.error { "응답을 파싱할 수 없습니다: $e" }
            throw ClientUnknownException("파일 업로드에 실패하였습니다.")
        }
    }

    fun remove(resource: Remove) {
        try {
            minioClient.removeObject(
                RemoveObjectArgs.builder()
                    .bucket(resource.bucket.bucketName)
                    .`object`(resource.objectName)
                    .build()
            )
        } catch (e: ErrorResponseException) {
            log.error { "MinIO 서버 오류: $e" }
            throw NetworkException("파일 삭제에 실패하였습니다.")
        } catch (e: ServerException) {
            log.error { "MinIO 서버 오류: $e" }
            throw NetworkException("파일 삭제에 실패하였습니다.")
        } catch (e: InsufficientDataException) {
            log.error { "파일의 데이터가 전송 도중 끊겼습니다: $e" }
            throw NetworkException("파일 삭제에 실패하였습니다.")
        } catch (e: InternalException) {
            log.error { "MinioClient 오류: $e" }
            throw NetworkException("파일 삭제에 실패하였습니다.")
        } catch (e: InvalidKeyException) {
            log.error { "암호화 키가 잘못되었습니다: $e" }
            throw CryptoException("파일 삭제에 실패하였습니다.")
        } catch (e: InvalidResponseException) {
            log.error { "알 수 없는 오류: $e" }
            throw ClientUnknownException("파일 삭제에 실패하였습니다.")
        } catch (e: IOException) {
            log.error { "파일 입출력 오류: $e" }
            throw NetworkException("파일 삭제에 실패하였습니다.")
        } catch (e: NoSuchAlgorithmException) {
            log.error { "암호화나 해시 등의 알고리즘이 지원되지 않습니다: $e" }
            throw CryptoException("파일 삭제에 실패하였습니다.")
        } catch (e: XmlParserException) {
            log.error { "응답을 파싱할 수 없습니다: $e" }
            throw ClientUnknownException("파일 삭제에 실패하였습니다.")
        }
    }

    fun removeAll(resource: RemoveAll) {
        minioClient.removeObjects(
            RemoveObjectsArgs.builder()
                .bucket(resource.bucket.bucketName)
                .objects(resource.objectNames.map { DeleteObject(it) })
                .build()
        )
    }

    companion object {
        private val log = KotlinLogging.logger {}
    }
}

