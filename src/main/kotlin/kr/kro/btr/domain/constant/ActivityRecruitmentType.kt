package kr.kro.btr.domain.constant

enum class ActivityRecruitmentType(val description: String) {
    RECRUITING("모집중"),
    CLOSED("정원마감"),
    ALREADY_PARTICIPATING("참여완료"),
    ENDED("종료")
}