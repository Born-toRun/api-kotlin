package kr.kro.btr.adapter.out.persistence

import kr.kro.btr.domain.entity.CrewEntity
import kr.kro.btr.domain.port.model.result.CrewRankingResult
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

    @Query(
        """
        SELECT new kr.kro.btr.domain.port.model.result.CrewRankingResult(
            c.id,
            c.name,
            c.contents,
            c.region,
            img.fileUri,
            logo.fileUri,
            c.sns,
            COUNT(a.id)
        )
        FROM CrewEntity c
        LEFT JOIN c.imageEntity img
        LEFT JOIN c.logoEntity logo
        LEFT JOIN c.userEntities u
        LEFT JOIN u.activityEntities a
        GROUP BY c.id, c.name, c.contents, c.region, img.fileUri, logo.fileUri, c.sns
        ORDER BY COUNT(a.id) DESC
        """
    )
    fun findCrewRankings(): List<CrewRankingResult>
}
