package kr.kro.btr.adapter.`in`.web.payload

data class DetailAnnounceResponse(
    val id: Long,
    val title: String,
    val contents: String,
    val writer: Writer
) {
    data class Writer(
        val userId: Long,
        val name: String
    )
}
