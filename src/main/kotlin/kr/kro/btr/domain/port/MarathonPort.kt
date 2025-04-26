package kr.kro.btr.domain.port

import kr.kro.btr.domain.port.model.BookmarkMarathonCommand
import kr.kro.btr.domain.port.model.CancelBookmarkMarathonCommand
import kr.kro.btr.domain.port.model.Marathon
import kr.kro.btr.domain.port.model.MarathonDetail
import kr.kro.btr.domain.port.model.SearchAllMarathonCommand
import kr.kro.btr.domain.port.model.SearchMarathonDetailCommand

interface MarathonPort {
    fun search(command: SearchAllMarathonCommand): List<Marathon>
    fun detail(command: SearchMarathonDetailCommand): MarathonDetail
    fun bookmark(command: BookmarkMarathonCommand): Long
    fun cancelBookmark(command: CancelBookmarkMarathonCommand): Long
}