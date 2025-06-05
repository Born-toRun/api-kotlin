package kr.kro.btr.core.service

import kr.kro.btr.base.extension.toBookmarkMarathonQuery
import kr.kro.btr.core.converter.MarathonConverter
import kr.kro.btr.domain.port.MarathonPort
import kr.kro.btr.domain.port.model.BookmarkMarathonCommand
import kr.kro.btr.domain.port.model.CancelBookmarkMarathonCommand
import kr.kro.btr.domain.port.model.Marathon
import kr.kro.btr.domain.port.model.MarathonDetail
import kr.kro.btr.domain.port.model.SearchAllMarathonCommand
import kr.kro.btr.domain.port.model.SearchMarathonDetailCommand
import kr.kro.btr.infrastructure.MarathonGateway
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MarathonService(
    private val marathonConverter: MarathonConverter,
    private val marathonGateway: MarathonGateway
) : MarathonPort {

    @Transactional(readOnly = true)
    override fun search(command: SearchAllMarathonCommand): List<Marathon> {
        val query = marathonConverter.map(command)
        val marathonEntities = marathonGateway.search(query)
        return marathonConverter.map(marathonEntities, command.myUserId)
    }

    @Transactional(readOnly = true)
    override fun detail(command: SearchMarathonDetailCommand): MarathonDetail {
        val marathonEntity = marathonGateway.detail(command.marathonId)
        return marathonConverter.map(marathonEntity, command.myUserId)
    }

    @Transactional
    override fun bookmark(command: BookmarkMarathonCommand): Long {
        val query = command.toBookmarkMarathonQuery()
        marathonGateway.bookmark(query)
        return command.marathonId
    }

    @Transactional
    override fun cancelBookmark(command: CancelBookmarkMarathonCommand): Long {
        val query = command.toBookmarkMarathonQuery()
        marathonGateway.cancelBookmark(query)
        return command.marathonId
    }
}
