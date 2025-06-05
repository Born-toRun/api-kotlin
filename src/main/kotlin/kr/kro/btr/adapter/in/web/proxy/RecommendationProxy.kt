package kr.kro.btr.adapter.`in`.web.proxy

import kr.kro.btr.base.extension.toCreateRecommendationCommand
import kr.kro.btr.base.extension.toRemoveRecommendationCommand
import kr.kro.btr.domain.constant.RecommendationType
import kr.kro.btr.domain.port.RecommendationPort
import kr.kro.btr.support.TokenDetail
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CacheEvict
import org.springframework.stereotype.Component

@Component
@CacheConfig(cacheNames = ["recommendation"])
class RecommendationProxy(
    private val recommendationPort: RecommendationPort
) {

    @CacheEvict(allEntries = true, cacheNames = ["recommendation", "feed"])
    fun create(my: TokenDetail, recommendationType: RecommendationType, contentId: Long) {
        val command = my.toCreateRecommendationCommand(recommendationType, contentId)
        recommendationPort.create(command)
    }

    @CacheEvict(allEntries = true, cacheNames = ["recommendation", "feed"])
    fun remove(my: TokenDetail, recommendationType: RecommendationType, contentId: Long) {
        val command = my.toRemoveRecommendationCommand(recommendationType, contentId)
        recommendationPort.remove(command)
    }
}
