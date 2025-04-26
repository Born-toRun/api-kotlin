package kr.kro.btr.adapter.out.persistence

import kr.kro.btr.domain.entity.UserPrivacyEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query


interface UserPrivacyRepository : JpaRepository<UserPrivacyEntity, Long> {

    @Query(
        """
        SELECT u FROM UserPrivacyEntity u 
        JOIN FETCH u.userEntity 
        WHERE u.userId = :userId
        """
    )
    fun findByUserId(userId: Long): UserPrivacyEntity?
}