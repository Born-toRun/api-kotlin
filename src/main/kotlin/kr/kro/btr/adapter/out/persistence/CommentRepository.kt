package kr.kro.btr.adapter.out.persistence

import kr.kro.btr.domain.entity.CommentEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface CommentRepository : JpaRepository<CommentEntity, Long> {

    fun findAllByParentId(parentId: Long): List<CommentEntity>

    @Query(
        """
        SELECT DISTINCT c FROM CommentEntity c 
        INNER JOIN FETCH c.feedEntity 
        INNER JOIN FETCH c.userEntity
        LEFT JOIN FETCH c.userEntity.profileImageEntity
        INNER JOIN FETCH c.userEntity.crewEntity
        LEFT JOIN FETCH c.recommendationEntities
        LEFT JOIN FETCH c.child
        WHERE c.feedId = :feedId
        """
    )
    fun findAllByFeedId(feedId: Long): List<CommentEntity>

    fun findAllByFeedIdIn(feedIds: List<Long>): List<CommentEntity>

    fun countByFeedId(feedId: Long): Int

    fun findAllByUserId(userId: Long): List<CommentEntity>

    @Query(
        """
        SELECT DISTINCT c FROM CommentEntity c 
        INNER JOIN FETCH c.feedEntity 
        INNER JOIN FETCH c.userEntity
        LEFT JOIN FETCH c.userEntity.profileImageEntity
        INNER JOIN FETCH c.userEntity.crewEntity
        LEFT JOIN FETCH c.recommendationEntities
        LEFT JOIN FETCH c.child
        WHERE c.id = :id
        """
    )
    fun findByIdOrNull(id: Long): CommentEntity?
}
