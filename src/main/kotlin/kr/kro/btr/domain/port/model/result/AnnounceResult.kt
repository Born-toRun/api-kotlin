package kr.kro.btr.domain.port.model.result

data class AnnounceResult(
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
