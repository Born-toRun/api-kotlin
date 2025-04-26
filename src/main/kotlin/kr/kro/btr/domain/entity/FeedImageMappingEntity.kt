package kr.kro.btr.domain.entity

import jakarta.persistence.*
import org.hibernate.annotations.DynamicInsert

@Entity
@Table(name = "feed_image_mapping")
@DynamicInsert
class FeedImageMappingEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,
    var feedId: Long = 0,
    var imageId: Long,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feedId", insertable = false, updatable = false)
    var feedEntity: FeedEntity? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "imageId", insertable = false, updatable = false)
    var objectStorageEntity: ObjectStorageEntity? = null
)
