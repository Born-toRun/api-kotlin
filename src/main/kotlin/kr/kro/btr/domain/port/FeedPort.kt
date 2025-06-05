package kr.kro.btr.domain.port

import kr.kro.btr.domain.port.model.CreateFeedCommand
import kr.kro.btr.domain.port.model.result.FeedResult
import kr.kro.btr.domain.port.model.result.FeedDetailResult
import kr.kro.btr.domain.port.model.ModifyFeedCommand
import kr.kro.btr.domain.port.model.RemoveFeedCommand
import kr.kro.btr.domain.port.model.SearchAllFeedCommand
import kr.kro.btr.domain.port.model.SearchFeedDetailCommand
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface FeedPort {
    fun searchDetail(command: SearchFeedDetailCommand): FeedDetailResult
    fun searchAll(command: SearchAllFeedCommand, pageable: Pageable): Page<FeedResult>
    fun increaseViewQty(feedId: Long)
    fun create(command: CreateFeedCommand)
    fun remove(command: RemoveFeedCommand)
    fun modify(command: ModifyFeedCommand)
}
