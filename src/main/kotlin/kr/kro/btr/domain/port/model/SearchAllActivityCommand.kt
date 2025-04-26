package kr.kro.btr.domain.port.model

import kr.kro.btr.domain.constant.ActivityRecruitmentType

data class SearchAllActivityCommand(
    val courses: List<String>,
    val recruitmentType: ActivityRecruitmentType,
    val myCrewId: Long?,
    val myUserId: Long
)
