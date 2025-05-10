package kr.kro.btr.adapter.out.persistence

import kr.kro.btr.domain.entity.ActivityParticipationEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ActivityParticipationRepository : JpaRepository<ActivityParticipationEntity, Long> {

    fun findAllByUserId(userId: Long): List<ActivityParticipationEntity>

    @Query(
        """
        SELECT DISTINCT a FROM ActivityParticipationEntity a 
        INNER JOIN FETCH a.activityEntity
        INNER JOIN FETCH a.activityEntity.userEntity
        INNER JOIN FETCH a.activityEntity.userEntity.crewEntity
        LEFT JOIN FETCH a.activityEntity.userEntity.profileImageEntity
        WHERE a.activityId = :activityId
        """
    )
    fun findAllByActivityId(activityId: Long): List<ActivityParticipationEntity>

    fun findByActivityIdAndUserId(activityId: Long, userId: Long): ActivityParticipationEntity?
}
