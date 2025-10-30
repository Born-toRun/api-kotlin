package kr.kro.btr.domain.model

import java.io.InputStream

/**
 * Domain 레이어에서 파일 업로드를 추상화한 인터페이스
 * Spring의 MultipartFile에 대한 의존성을 제거하기 위한 래퍼
 */
interface FileUpload {
    val originalFilename: String?
    val size: Long
    val inputStream: InputStream
    val contentType: String?
}
