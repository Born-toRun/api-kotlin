package kr.kro.btr.adapter.out.persistence

import kr.kro.btr.domain.entity.MarathonEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface MarathonRepository : JpaRepository<MarathonEntity, Long> {

    @Query(
        """
        SELECT m FROM MarathonEntity m 
        LEFT JOIN FETCH m.marathonBookmarkEntities mb 
        WHERE (m.location IN :locations OR :locations IS NULL) AND 
              (m.course IN :courses OR :courses IS NULL)
        """
    )
    fun findAllByLocationInAndCourseIn(
        locations: List<String>?,
        courses: List<String>?
    ): List<MarathonEntity>

    @Query(
        """
        SELECT m FROM MarathonEntity m 
        LEFT JOIN FETCH m.marathonBookmarkEntities mb 
        WHERE m.id = :marathonId
        """
    )
    fun findByIdOrNull(marathonId: Long): MarathonEntity?
}
