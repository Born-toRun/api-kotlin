package kr.kro.btr.adapter.`in`.web.payload

data class MyParticipationsResponse(
    val participations: List<Participation>
) {
    data class Participation(
        val activityId: Long,
        val title: String,
        val startAt: String,
        val course: String?
    )
}
