package kr.kro.btr.adapter.out.persistence

import kr.kro.btr.domain.entity.FeedEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface FeedRepository : JpaRepository<FeedEntity, Long> {

    @Query(
        """
        SELECT DISTINCT f FROM FeedEntity f 
        INNER JOIN FETCH f.userEntity
        LEFT JOIN FETCH f.commentEntities 
        LEFT JOIN FETCH f.feedImageMappingEntities 
        LEFT JOIN FETCH f.recommendationEntities 
        WHERE f.id = :id
        """
    )
    fun findByIdOrNull(id: Long): FeedEntity?
}
