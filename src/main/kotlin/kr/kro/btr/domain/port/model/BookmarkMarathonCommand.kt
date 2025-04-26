package kr.kro.btr.domain.port.model

data class BookmarkMarathonCommand(
    val marathonId: Long,
    val myUserId: Long
)