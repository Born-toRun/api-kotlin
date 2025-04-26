package kr.kro.btr.domain.port

import kr.kro.btr.domain.port.model.CreateRecommendationCommand
import kr.kro.btr.domain.port.model.RemoveRecommendationCommand

interface RecommendationPort {
    fun create(command: CreateRecommendationCommand)
    fun remove(command: RemoveRecommendationCommand)
}