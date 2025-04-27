package kr.kro.btr.domain.entity

import jakarta.persistence.*
import org.hibernate.annotations.DynamicInsert

@Entity
@Table(name = "feed_image_mapping")
@DynamicInsert
class FeedImageMappingEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val feedId: Long = 0,
    val imageId: Long,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feedId", insertable = false, updatable = false)
    var feedEntity: FeedEntity? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "imageId", insertable = false, updatable = false)
    val objectStorageEntity: ObjectStorageEntity? = null
)
