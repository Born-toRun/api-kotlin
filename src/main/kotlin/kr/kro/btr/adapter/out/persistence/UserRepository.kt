package kr.kro.btr.adapter.out.persistence

import kr.kro.btr.domain.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface UserRepository : JpaRepository<UserEntity, Long> {

    @Query(
        """
        SELECT u FROM UserEntity u 
        LEFT JOIN FETCH u.crewEntity 
        LEFT JOIN FETCH u.profileImageEntity 
        WHERE u.id = :id
        """
    )
    fun findByIdOrNull(id: Long): UserEntity?

    @Query(
        """
        SELECT DISTINCT u FROM UserEntity u 
        LEFT JOIN FETCH u.crewEntity 
        LEFT JOIN FETCH u.profileImageEntity 
        LEFT JOIN FETCH u.userRefreshTokenEntity 
        LEFT JOIN FETCH u.userPrivacyEntity 
        LEFT JOIN FETCH u.feedEntities 
        LEFT JOIN FETCH u.activityEntities 
        LEFT JOIN FETCH u.activityParticipationEntities 
        LEFT JOIN FETCH u.commentEntities 
        LEFT JOIN FETCH u.marathonBookmarkEntities 
        LEFT JOIN FETCH u.recommendationEntities 
        WHERE u.id = :id
        """
    )
    fun findAllEntitiesById(id: Long): UserEntity?

    fun findAllByNameContaining(userName: String): List<UserEntity>

    @Query(
        """
        SELECT u FROM UserEntity u 
        LEFT JOIN FETCH u.profileImageEntity 
        LEFT JOIN FETCH u.userPrivacyEntity 
        WHERE u.socialId = :socialId
        """
    )
    fun findBySocialId(socialId: String): UserEntity?

    fun existsBySocialId(socialId: String): Boolean
}
