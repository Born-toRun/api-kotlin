package kr.kro.btr.adapter.out.persistence

import kr.kro.btr.domain.entity.UserEntity
import kr.kro.btr.domain.port.model.result.CrewMemberRankingResult
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface UserRepository : JpaRepository<UserEntity, Long> {

    @Query(
        """
        SELECT u FROM UserEntity u
        LEFT JOIN FETCH u.crewEntity
        INNER JOIN FETCH u.userPrivacyEntity
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

    @Query(
        """
        SELECT u FROM UserEntity u
        LEFT JOIN FETCH u.profileImageEntity
        LEFT JOIN FETCH u.userPrivacyEntity
        WHERE u.crewId = :crewId
        """
    )
    fun findAllByCrewId(crewId: Long): List<UserEntity>

    @Query(
        """
        SELECT new kr.kro.btr.domain.port.model.result.CrewMemberRankingResult(
            u.id,
            u.name,
            img.fileUri,
            u.instagramId,
            COUNT(ap.id)
        )
        FROM UserEntity u
        LEFT JOIN u.profileImageEntity img
        LEFT JOIN u.activityParticipationEntities ap
        WHERE u.crewId = :crewId
        GROUP BY u.id, u.name, img.fileUri, u.instagramId
        ORDER BY COUNT(ap.id) DESC
        """
    )
    fun findCrewMemberRankings(crewId: Long): List<CrewMemberRankingResult>
}
