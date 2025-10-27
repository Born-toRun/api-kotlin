package kr.kro.btr.domain.port.model

import kr.kro.btr.domain.constant.ActivityRecruitmentType

data class SearchByCrewIdActivityCommand(
    val crewId: Long,
    val courses: List<String>?,
    val recruitmentType: ActivityRecruitmentType?
)