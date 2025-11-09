package kr.kro.btr.adapter.out.persistence

import kr.kro.btr.domain.entity.MarathonBookmarkEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface MarathonBookmarkRepository : JpaRepository<MarathonBookmarkEntity, Long> {
    fun findByUserIdAndMarathonId(userId: Long, marathonId: Long): MarathonBookmarkEntity?

    fun existsByMarathonIdAndUserId(marathonId: Long, userId: Long): Boolean

    @Query(
        """
        SELECT mb.marathonId
        FROM MarathonBookmarkEntity mb
        WHERE mb.userId = :userId
        AND mb.marathonId IN :marathonIds
        """
    )
    fun findBookmarkedMarathonIds(userId: Long, marathonIds: List<Long>): List<Long>
}
