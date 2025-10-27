package kr.kro.btr.domain.constant

enum class ActivityRecruitmentType(val description: String) {
    RECRUITING("모집중"),
    CLOSED("종료"),
    FULL("정원마감"),
    ALREADY_PARTICIPATING("참여완료")
}