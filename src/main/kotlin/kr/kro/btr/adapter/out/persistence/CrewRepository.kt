package kr.kro.btr.adapter.out.persistence

import kr.kro.btr.domain.entity.CrewEntity
import org.springframework.data.jpa.repository.JpaRepository

interface CrewRepository : JpaRepository<CrewEntity, Long>