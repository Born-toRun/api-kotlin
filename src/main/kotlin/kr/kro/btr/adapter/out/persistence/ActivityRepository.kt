package kr.kro.btr.adapter.out.persistence

import kr.kro.btr.domain.entity.ActivityEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDateTime

interface ActivityRepository : JpaRepository<ActivityEntity, Long> {

    @Query(
        """
        SELECT a FROM ActivityEntity a
        INNER JOIN FETCH a.userEntity u
        LEFT JOIN FETCH u.profileImageEntity
        INNER JOIN FETCH u.crewEntity
        WHERE a.id = :id
        """
    )
    fun findByIdOrNull(id: Long): ActivityEntity?
    fun findByStartAtAndUserId(startAt: LocalDateTime, userId: Long): ActivityEntity?
}
