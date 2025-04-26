package kr.kro.btr.infrastructure.model

import kr.kro.btr.domain.constant.ActivityRecruitmentType


data class SearchAllActivityQuery(
    val courses: List<String>,
    val recruitmentType: ActivityRecruitmentType?,
    val myCrewId: Long?,
    val myUserId: Long
)