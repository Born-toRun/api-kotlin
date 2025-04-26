package kr.kro.btr.domain.constant

enum class RoleType(val code: String, val description: String) {
    ADMIN("ROLE_ADMIN", "관리자"),
    MANAGER("ROLE_MANAGER", "운영진"),
    MEMBER("ROLE_MEMBER", "크루원"),
    GUEST("ROLE_GUEST", "신규가입자");

    companion object {
        fun of(code: String): RoleType {
            return RoleType.entries.find { it.code == code } ?: GUEST
        }
    }
}
