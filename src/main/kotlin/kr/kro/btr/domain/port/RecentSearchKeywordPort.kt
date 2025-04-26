package kr.kro.btr.domain.port

interface RecentSearchKeywordPort {
    fun removeAll(userId: Long)
    fun removeKeyword(userId: Long, searchKeyword: String)
    fun add(userId: Long, searchKeyword: String)
    fun search(userId: Long): List<Any> // TODO: 타입 Any 말고
}