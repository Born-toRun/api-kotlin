package kr.kro.btr.core.converter

import kr.kro.btr.adapter.`in`.web.payload.SearchAllMarathonRequest
import kr.kro.btr.adapter.`in`.web.payload.SearchAllMarathonResponse
import kr.kro.btr.adapter.`in`.web.payload.SearchMarathonDetailResponse
import kr.kro.btr.domain.entity.MarathonEntity
import kr.kro.btr.domain.port.model.BookmarkMarathonCommand
import kr.kro.btr.domain.port.model.CancelBookmarkMarathonCommand
import kr.kro.btr.domain.port.model.Marathon
import kr.kro.btr.domain.port.model.MarathonDetail
import kr.kro.btr.domain.port.model.SearchAllMarathonCommand
import kr.kro.btr.domain.port.model.SearchMarathonDetailCommand
import kr.kro.btr.infrastructure.model.SearchMarathonQuery
import org.springframework.stereotype.Component
import kotlin.Long
import kotlin.collections.List

@Component
class MarathonConverter {

    fun map(source: List<Marathon>): SearchAllMarathonResponse {
        val marathons = source.map { marathon ->
            SearchAllMarathonResponse.Marathon(
                id = marathon.id,
                title = marathon.title,
                schedule = marathon.schedule,
                venue = marathon.venue,
                course = marathon.course,
                isBookmarking = marathon.isBookmarking == true
            )
        }
        return SearchAllMarathonResponse(
            marathons = marathons
        )
    }

    fun map(source: MarathonDetail): SearchMarathonDetailResponse {
        return SearchMarathonDetailResponse(
            id = source.id,
            title = source.title,
            owner = source.owner,
            email = source.email,
            schedule = source.schedule,
            contact = source.contact,
            course = source.course,
            location = source.location,
            venue = source.venue,
            host = source.host,
            duration = source.duration,
            homepage = source.homepage,
            venueDetail = source.venueDetail,
            remark = source.remark,
            registeredAt = source.registeredAt,
            isBookmarking = source.isBookmarking
        )
    }

    fun map(source: SearchAllMarathonRequest, userId: Long): SearchAllMarathonCommand {
        return SearchAllMarathonCommand(
            locations = source.locations,
            courses = source.courses,
            myUserId = userId
        )
    }
    fun mapToSearchMarathonDetailCommand(marathonId: Long, userId: Long): SearchMarathonDetailCommand {
        return SearchMarathonDetailCommand(
            marathonId = marathonId,
            myUserId = userId
        )
    }
    fun mapToBookmarkMarathonCommand(marathonId: Long, userId: Long): BookmarkMarathonCommand {
        return BookmarkMarathonCommand(
            marathonId = marathonId,
            myUserId = userId
        )
    }
    fun mapToCancelBookmarkMarathonCommand(marathonId: Long, userId: Long): CancelBookmarkMarathonCommand {
        return CancelBookmarkMarathonCommand(
            marathonId = marathonId,
            myUserId = userId
        )
    }

    fun map(source: SearchAllMarathonCommand): SearchMarathonQuery {
        return SearchMarathonQuery(
            locations = source.locations,
            courses = source.courses
        )
    }
    fun map(source: List<MarathonEntity>, userId: Long): List<Marathon> {
        return source.map { marathon ->
            Marathon(
                id = marathon.id,
                title = marathon.title,
                schedule = marathon.schedule,
                venue = marathon.venue,
                course = marathon.course,
                isBookmarking = marathon.marathonBookmarkEntities.any { userId == it.userId }
            )
        }
    }

    fun map(source: MarathonEntity, userId: Long): MarathonDetail {
        return MarathonDetail(
            id = source.id,
            title = source.title,
            owner = source.owner,
            email = source.email,
            schedule = source.schedule,
            contact = source.contact,
            course = source.course,
            location = source.location,
            venue = source.venue,
            host = source.host,
            duration = source.duration,
            homepage = source.homepage,
            venueDetail = source.venueDetail,
            remark = source.remark,
            registeredAt = source.registeredAt,
            isBookmarking = source.marathonBookmarkEntities.any { userId == it.userId }
        )
    }
}
