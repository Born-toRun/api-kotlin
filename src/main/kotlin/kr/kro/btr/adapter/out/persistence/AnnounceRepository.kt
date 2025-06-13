package kr.kro.btr.adapter.out.persistence

import kr.kro.btr.domain.entity.AnnounceEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface AnnounceRepository : JpaRepository<AnnounceEntity, Long> {
    @Query(
        """
        SELECT a FROM AnnounceEntity a 
        INNER JOIN FETCH a.userEntity
        WHERE a.postedAt <= now()
        """
    )
    override fun findAll(): List<AnnounceEntity>
}
