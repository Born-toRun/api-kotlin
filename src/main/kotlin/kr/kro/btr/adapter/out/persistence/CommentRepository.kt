package kr.kro.btr.adapter.out.persistence

import kr.kro.btr.domain.entity.CommentEntity
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface CommentRepository : JpaRepository<CommentEntity, Long> {

    @EntityGraph(attributePaths = [
        "userEntity",
        "userEntity.profileImageEntity",
        "userEntity.crewEntity"
    ])
    fun findAllByParentId(parentId: Long): List<CommentEntity>

    @EntityGraph(attributePaths = [
        "feedEntity",
        "userEntity",
        "userEntity.profileImageEntity",
        "userEntity.crewEntity"
    ])
    fun findAllByFeedIdOrderByIdDesc(feedId: Long): List<CommentEntity>

    @EntityGraph(attributePaths = [
        "userEntity",
        "userEntity.profileImageEntity",
        "userEntity.crewEntity"
    ])

    fun countByFeedId(feedId: Long): Int

    fun existsByFeedIdAndUserId(feedId: Long, userId: Long): Boolean


    @Query(
        """
        SELECT DISTINCT c FROM CommentEntity c
        INNER JOIN FETCH c.feedEntity
        INNER JOIN FETCH c.userEntity u
        LEFT JOIN FETCH u.profileImageEntity
        LEFT JOIN FETCH u.crewEntity
        WHERE c.id = :id
        """
    )
    fun findByIdOrNull(id: Long): CommentEntity?

    @Query(
        """
        SELECT DISTINCT c FROM CommentEntity c
        INNER JOIN FETCH c.userEntity u
        LEFT JOIN FETCH u.profileImageEntity
        LEFT JOIN FETCH u.crewEntity
        WHERE c.id = :id
        """
    )
    fun findByIdWithReplies(id: Long): CommentEntity?

    @Query(
        """
        SELECT c.feedId as feedId, COUNT(c.id) as count
        FROM CommentEntity c
        WHERE c.feedId IN :feedIds
        GROUP BY c.feedId
        """
    )
    fun countGroupByFeedId(feedIds: List<Long>): List<CommentCount>

    @Query(
        """
        SELECT DISTINCT c.feedId
        FROM CommentEntity c
        WHERE c.userId = :userId
        AND c.feedId IN :feedIds
        """
    )
    fun findUserCommentedFeedIds(userId: Long, feedIds: List<Long>): List<Long>

    @Query(
        """
        SELECT c.parentId as parentId, COUNT(c.id) as count
        FROM CommentEntity c
        WHERE c.parentId IN :parentIds
        AND c.parentId IS NOT NULL
        GROUP BY c.parentId
        """
    )
    fun countGroupByParentId(parentIds: List<Long>): List<ReplyCount>

    interface CommentCount {
        fun getFeedId(): Long
        fun getCount(): Long
    }

    interface ReplyCount {
        fun getParentId(): Long
        fun getCount(): Long
    }
}
