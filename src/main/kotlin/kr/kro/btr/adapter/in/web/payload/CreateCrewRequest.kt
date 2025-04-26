package kr.kro.btr.adapter.`in`.web.payload

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CreateCrewRequest(
    @NotBlank @Size(max = 255) val name: String,
    @NotBlank @Size(max = 255) val contents: String,
    @Size(max = 255) val sns: String?,
    @NotBlank @Size(max = 50) val region: String
)
