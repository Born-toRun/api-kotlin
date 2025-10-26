package kr.kro.btr.domain.entity

import jakarta.persistence.*
import org.hibernate.annotations.DynamicInsert

@Entity
@Table(name = "activity_image_mapping")
@DynamicInsert
class ActivityImageMappingEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val activityId: Long = 0,
    val imageId: Long,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activityId", insertable = false, updatable = false)
    var activityEntity: ActivityEntity? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "imageId", insertable = false, updatable = false)
    val objectStorageEntity: ObjectStorageEntity? = null
)
