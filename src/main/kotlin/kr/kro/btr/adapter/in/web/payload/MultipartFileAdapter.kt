package kr.kro.btr.adapter.`in`.web.payload

import kr.kro.btr.domain.model.FileUpload
import org.springframework.web.multipart.MultipartFile
import java.io.InputStream

/**
 * Spring의 MultipartFile을 Domain의 FileUpload로 변환하는 어댑터
 */
class MultipartFileAdapter(
    private val multipartFile: MultipartFile
) : FileUpload {
    override val originalFilename: String?
        get() = multipartFile.originalFilename

    override val size: Long
        get() = multipartFile.size

    override val inputStream: InputStream
        get() = multipartFile.inputStream

    override val contentType: String?
        get() = multipartFile.contentType
}

/**
 * MultipartFile을 FileUpload로 변환하는 확장 함수
 */
fun MultipartFile.toFileUpload(): FileUpload = MultipartFileAdapter(this)
