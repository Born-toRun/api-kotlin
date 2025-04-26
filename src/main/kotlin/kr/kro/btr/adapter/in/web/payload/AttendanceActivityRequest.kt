package kr.kro.btr.adapter.`in`.web.payload

import jakarta.validation.constraints.NotNull

data class AttendanceActivityRequest(
    @NotNull val accessCode: Int
)
