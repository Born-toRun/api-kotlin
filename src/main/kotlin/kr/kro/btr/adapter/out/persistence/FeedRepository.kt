package kr.kro.btr.adapter.out.persistence

import kr.kro.btr.domain.entity.FeedEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface FeedRepository : JpaRepository<FeedEntity, Long> {

    @Query(
        """
        SELECT f FROM FeedEntity f
        INNER JOIN FETCH f.userEntity u
        LEFT JOIN FETCH u.profileImageEntity
        LEFT JOIN FETCH u.crewEntity
        WHERE f.id = :id
        """
    )
    fun findByIdOrNull(id: Long): FeedEntity?

    @Query(
        """
        SELECT DISTINCT f FROM FeedEntity f
        INNER JOIN FETCH f.userEntity u
        LEFT JOIN FETCH u.profileImageEntity
        LEFT JOIN FETCH u.crewEntity
        LEFT JOIN FETCH f.feedImageMappingEntities fim
        LEFT JOIN FETCH fim.objectStorageEntity
        WHERE f.userId = :userId
        ORDER BY f.registeredAt DESC
        """
    )
    fun findAllByUserIdWithImages(userId: Long): List<FeedEntity>
}
