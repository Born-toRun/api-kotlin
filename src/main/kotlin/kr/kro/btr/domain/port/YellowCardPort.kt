package kr.kro.btr.domain.port

import kr.kro.btr.domain.port.model.CreateYellowCardCommand

interface YellowCardPort {
    fun create(command: CreateYellowCardCommand)
}