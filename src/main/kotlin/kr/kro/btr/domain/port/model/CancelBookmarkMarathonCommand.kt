package kr.kro.btr.domain.port.model

data class CancelBookmarkMarathonCommand(
    val marathonId: Long,
    val myUserId: Long
)