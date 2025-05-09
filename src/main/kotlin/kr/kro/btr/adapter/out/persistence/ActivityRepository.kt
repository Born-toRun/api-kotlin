package kr.kro.btr.adapter.out.persistence

import kr.kro.btr.domain.entity.ActivityEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDateTime

interface ActivityRepository : JpaRepository<ActivityEntity, Long> {

    @Query(
        """
        SELECT DISTINCT a FROM ActivityEntity a 
        LEFT JOIN FETCH a.activityParticipationEntities
        INNER JOIN FETCH a.userEntity
        LEFT JOIN FETCH a.userEntity.profileImageEntity
        INNER JOIN FETCH a.userEntity.crewEntity
        WHERE a.id = :id
        """
    )
    fun findByIdOrNull(id: Long): ActivityEntity?
    fun findAllByUserId(userId: Long): List<ActivityEntity>
    fun findByStartAtAndUserId(startAt: LocalDateTime, userId: Long): ActivityEntity?
}
