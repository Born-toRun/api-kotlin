package kr.kro.btr.adapter.out.persistence

import kr.kro.btr.domain.entity.ActivityParticipationEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ActivityParticipationRepository : JpaRepository<ActivityParticipationEntity, Long> {

    @Query(
        """
        SELECT DISTINCT a FROM ActivityParticipationEntity a
        INNER JOIN FETCH a.activityEntity ae
        INNER JOIN FETCH ae.userEntity u
        LEFT JOIN FETCH u.profileImageEntity
        LEFT JOIN FETCH u.crewEntity
        WHERE a.userId = :userId
        """
    )
    fun findAllByUserId(userId: Long): List<ActivityParticipationEntity>

    @Query(
        """
        SELECT DISTINCT a FROM ActivityParticipationEntity a
        INNER JOIN FETCH a.activityEntity
        INNER JOIN FETCH a.activityEntity.userEntity
        LEFT JOIN FETCH a.activityEntity.userEntity.crewEntity
        LEFT JOIN FETCH a.activityEntity.userEntity.profileImageEntity
        WHERE a.activityId = :activityId
        """
    )
    fun findAllByActivityId(activityId: Long): List<ActivityParticipationEntity>

    fun findByActivityIdAndUserId(activityId: Long, userId: Long): ActivityParticipationEntity?

    fun countByActivityId(activityId: Long): Int

    fun existsByActivityIdAndUserId(activityId: Long, userId: Long): Boolean

    @Query(
        """
        SELECT a.activityId as activityId, COUNT(a.id) as count
        FROM ActivityParticipationEntity a
        WHERE a.activityId IN :activityIds
        GROUP BY a.activityId
        """
    )
    fun countGroupByActivityId(activityIds: List<Long>): List<Map<String, Any>>

    @Query(
        """
        SELECT a.activityId
        FROM ActivityParticipationEntity a
        WHERE a.userId = :userId
        AND a.activityId IN :activityIds
        """
    )
    fun findUserParticipatedActivityIds(userId: Long, activityIds: List<Long>): List<Long>
}
