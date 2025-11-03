package kr.kro.btr.adapter.out.persistence

import kr.kro.btr.domain.entity.FeedbackEntity
import org.springframework.data.jpa.repository.JpaRepository

interface FeedbackRepository : JpaRepository<FeedbackEntity, Long>
