package kr.kro.btr.adapter.out.persistence

import kr.kro.btr.domain.entity.CrewEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface CrewRepository : JpaRepository<CrewEntity, Long> {

    @Query(
        """
        SELECT c FROM CrewEntity c 
        LEFT JOIN FETCH c.imageEntity
        LEFT JOIN FETCH c.logoEntity 
        """
    )
    override fun findAll(): List<CrewEntity>

    @Query(
        """
            SELECT c FROM CrewEntity c
                LEFT JOIN FETCH c.imageEntity
                LEFT JOIN FETCH c.logoEntity
                LEFT JOIN FETCH c.userEntities
            WHERE c.id = :id
        """
    )
    fun findByIdOrNull(id: Long): CrewEntity?
}
