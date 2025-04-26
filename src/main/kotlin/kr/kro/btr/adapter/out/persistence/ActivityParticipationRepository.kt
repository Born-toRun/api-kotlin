package kr.kro.btr.adapter.out.persistence

import kr.kro.btr.domain.entity.ActivityParticipationEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ActivityParticipationRepository : JpaRepository<ActivityParticipationEntity, Long> {

    fun findAllByUserId(userId: Long): List<ActivityParticipationEntity>

    fun findAllByActivityId(activityId: Long): List<ActivityParticipationEntity>

    fun findByActivityIdAndUserId(activityId: Long, userId: Long): ActivityParticipationEntity?
}
