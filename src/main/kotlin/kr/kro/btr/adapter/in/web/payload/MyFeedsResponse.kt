package kr.kro.btr.adapter.`in`.web.payload

data class MyFeedsResponse(
    val feeds: List<Feed>
) {
    data class Feed(
        val feedId: Long,
        val contents: String
    )
}
