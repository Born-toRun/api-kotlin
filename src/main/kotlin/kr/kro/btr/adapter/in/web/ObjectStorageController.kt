package kr.kro.btr.adapter.`in`.web


import kr.kro.btr.adapter.`in`.web.payload.UploadFileResponse
import kr.kro.btr.adapter.`in`.web.proxy.ObjectStorageProxy
import kr.kro.btr.base.extension.toUploadFileResponse
import kr.kro.btr.domain.constant.Bucket
import kr.kro.btr.domain.port.model.ObjectStorage
import kr.kro.btr.support.TokenDetail
import kr.kro.btr.support.annotation.AuthUser
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/v1/object-storage")
class ObjectStorageController(
    private val objectStorageProxy: ObjectStorageProxy
) {

    @PostMapping(
        value = ["/{bucket}"],
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE]
    )
    fun uploadFile(
        @AuthUser my: TokenDetail,
        @PathVariable bucket: Bucket,
        @RequestParam("file") file: MultipartFile
    ): ResponseEntity<UploadFileResponse> {
        val objectStorage: ObjectStorage = objectStorageProxy.upload(my, bucket, file)
        val response: UploadFileResponse = objectStorage.toUploadFileResponse()
        return ResponseEntity.ok(response)
    }

    @DeleteMapping(value = ["/{bucket}/{fileId}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun removeFile(@AuthUser my: TokenDetail, @PathVariable bucket: Bucket, @PathVariable fileId: Long): ResponseEntity<Void> {
        objectStorageProxy.remove(my, bucket, fileId)
        return ResponseEntity.ok().build()
    }
}
