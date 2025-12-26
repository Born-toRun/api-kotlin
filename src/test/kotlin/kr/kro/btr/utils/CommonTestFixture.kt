package kr.kro.btr.utils

import org.springframework.mock.web.MockMultipartFile

object CommonTestFixture {

	fun getMultipartFile(): MockMultipartFile {
		val path = "file.png"
		val contentType = "image/png"

		return MockMultipartFile("file", path, contentType, "file".toByteArray())
	}
}
