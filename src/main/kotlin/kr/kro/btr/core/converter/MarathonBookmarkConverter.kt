package kr.kro.btr.core.converter

import kr.kro.btr.domain.entity.MarathonBookmarkEntity
import kr.kro.btr.domain.port.model.BookmarkMarathonCommand
import kr.kro.btr.domain.port.model.CancelBookmarkMarathonCommand
import kr.kro.btr.infrastructure.model.BookmarkMarathonQuery
import org.springframework.stereotype.Component

@Component
class MarathonBookmarkConverter {

    fun map(source: BookmarkMarathonCommand): BookmarkMarathonQuery {
        return BookmarkMarathonQuery(
            marathonId = source.marathonId,
            myUserId = source.myUserId
        )
    }

    fun map(source: CancelBookmarkMarathonCommand): BookmarkMarathonQuery {
        return BookmarkMarathonQuery(
            marathonId = source.marathonId,
            myUserId = source.myUserId
        )
    }

    fun map(source: BookmarkMarathonQuery): MarathonBookmarkEntity {
        return MarathonBookmarkEntity(
            userId = source.myUserId,
            marathonId = source.marathonId,
        )
    }
}