package kr.kro.btr.adapter.out.persistence

import kr.kro.btr.domain.entity.CommentEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface CommentRepository : JpaRepository<CommentEntity, Long> {

    fun findAllByParentId(parentId: Long): List<CommentEntity>

    @Query(
        """
        SELECT DISTINCT c FROM CommentEntity c 
        LEFT JOIN FETCH c.feedEntity 
        LEFT JOIN FETCH c.userEntity 
        LEFT JOIN FETCH c.recommendationEntities 
        WHERE c.feedId = :feedId
        """
    )
    fun findAllByFeedId(feedId: Long): List<CommentEntity>

    fun findAllByFeedIdIn(feedIds: List<Long>): List<CommentEntity>

    fun countByFeedId(feedId: Long): Int

    fun findAllByUserId(userId: Long): List<CommentEntity>
}