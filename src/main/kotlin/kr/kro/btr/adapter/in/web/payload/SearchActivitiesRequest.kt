package kr.kro.btr.adapter.`in`.web.payload

import kr.kro.btr.domain.constant.ActivityRecruitmentType

data class SearchActivitiesRequest(
    val courses: List<String>? = null,
    val recruitmentType: ActivityRecruitmentType? = null,
)
