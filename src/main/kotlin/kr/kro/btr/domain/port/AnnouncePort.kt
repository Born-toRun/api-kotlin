package kr.kro.btr.domain.port

import kr.kro.btr.domain.port.model.CreateAnnounceCommand
import kr.kro.btr.domain.port.model.ModifyAnnounceCommand
import kr.kro.btr.domain.port.model.result.AnnounceResult

interface AnnouncePort {

    fun create(command: CreateAnnounceCommand)
    fun searchAll(): List<AnnounceResult>
    fun detail(announceId: Long): AnnounceResult
    fun modify(command: ModifyAnnounceCommand): AnnounceResult
    fun remove(announceId: Long)
}
