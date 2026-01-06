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
        LEFT JOIN FETCH u.profileImageEntity
        LEFT JOIN FETCH u.userPrivacyEntity
        WHERE u.id = :id
        """
    )
    fun findAllEntitiesById(id: Long): UserEntity?

    @Query(
        """
        SELECT DISTINCT u FROM UserEntity u
        LEFT JOIN FETCH u.crewEntity
        LEFT JOIN FETCH u.profileImageEntity
        WHERE u.name LIKE %:userName%
        """
    )
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
            COALESCE(u.name, ''),
            COALESCE(img.fileUri, ''),
            COALESCE(u.instagramId, ''),
            CAST(COUNT(ap.id) AS int),
            CASE WHEN u.roleType = kr.kro.btr.domain.constant.RoleType.ADMIN THEN true ELSE false END,
            CASE WHEN u.managedCrewId IS NOT NULL THEN true ELSE false END
        )
        FROM UserEntity u
        LEFT JOIN ObjectStorageEntity img ON img.id = u.imageId
        LEFT JOIN ActivityParticipationEntity ap ON ap.userId = u.id
        WHERE u.crewId = :crewId
        GROUP BY u.id, u.name, img.fileUri, u.instagramId, u.roleType, u.managedCrewId
        ORDER BY COUNT(ap.id) DESC
        """
    )
    fun findCrewMemberRankings(crewId: Long): List<CrewMemberRankingResult>
}
