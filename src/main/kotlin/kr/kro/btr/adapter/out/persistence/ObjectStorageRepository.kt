package kr.kro.btr.adapter.out.persistence

import kr.kro.btr.domain.entity.ObjectStorageEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ObjectStorageRepository : JpaRepository<ObjectStorageEntity, Long>
