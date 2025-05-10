package kr.kro.btr.infrastructure

import kr.kro.btr.adapter.out.persistence.MarathonBookmarkRepository
import kr.kro.btr.adapter.out.persistence.MarathonRepository
import kr.kro.btr.core.converter.MarathonBookmarkConverter
import kr.kro.btr.domain.entity.MarathonEntity
import kr.kro.btr.infrastructure.model.BookmarkMarathonQuery
import kr.kro.btr.infrastructure.model.SearchMarathonQuery
import kr.kro.btr.support.exception.NotFoundException
import org.springframework.stereotype.Component

@Component
class MarathonGateway(
    private val marathonBookmarkConverter: MarathonBookmarkConverter,
    private val marathonRepository: MarathonRepository,
    private val marathonBookmarkRepository: MarathonBookmarkRepository
) {

    fun search(query: SearchMarathonQuery): List<MarathonEntity> {
        return marathonRepository.findAllByLocationInAndCourseIn(query.locations, query.courses)
    }

    fun detail(marathonId: Long): MarathonEntity {
        return marathonRepository.findByIdOrNull(marathonId) ?: throw NotFoundException("해당 대회를 찾을 수 없습니다.")
    }

    fun bookmark(query: BookmarkMarathonQuery) {
        val bookmark = marathonBookmarkRepository.findByUserIdAndMarathonId(query.myUserId, query.marathonId)
            ?: marathonBookmarkConverter.map(query)
        marathonBookmarkRepository.save(bookmark)
    }

    fun cancelBookmark(query: BookmarkMarathonQuery) {
        val bookmark = marathonBookmarkRepository.findByUserIdAndMarathonId(query.myUserId, query.marathonId)
            ?: throw NotFoundException("북마크가 되지 않았거나 이미 취소되었습니다.")
        marathonBookmarkRepository.deleteById(bookmark.id)
    }
}
