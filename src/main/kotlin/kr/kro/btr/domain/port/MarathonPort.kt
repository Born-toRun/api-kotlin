package kr.kro.btr.domain.port

import kr.kro.btr.domain.port.model.BookmarkMarathonCommand
import kr.kro.btr.domain.port.model.CancelBookmarkMarathonCommand
import kr.kro.btr.domain.port.model.result.MarathonResult
import kr.kro.btr.domain.port.model.result.MarathonDetailResult
import kr.kro.btr.domain.port.model.SearchAllMarathonCommand
import kr.kro.btr.domain.port.model.SearchMarathonDetailCommand

interface MarathonPort {
    fun search(command: SearchAllMarathonCommand): List<MarathonResult>
    fun detail(command: SearchMarathonDetailCommand): MarathonDetailResult
    fun bookmark(command: BookmarkMarathonCommand): Long
    fun cancelBookmark(command: CancelBookmarkMarathonCommand): Long
}
