package kr.kro.btr.domain.entity

import jakarta.persistence.*
import kr.kro.btr.domain.constant.FeedAccessLevel
import kr.kro.btr.domain.constant.FeedCategory
import kr.kro.btr.domain.model.ModifyFeedQuery
import org.hibernate.annotations.BatchSize
import org.hibernate.annotations.DynamicInsert
import java.time.LocalDateTime

@Entity
@Table(name = "feed")
@DynamicInsert
// TODO: private consttructor
class FeedEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val userId: Long = 0,
    var contents: String,
    @Enumerated(EnumType.STRING)
    var category: FeedCategory,
    @Enumerated(EnumType.STRING)
    var accessLevel: FeedAccessLevel,
    var viewQty: Int = 0,
    val registeredAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    val userEntity: UserEntity? = null
) {
    @OneToMany(mappedBy = "feedEntity", cascade = [CascadeType.REMOVE], orphanRemoval = true, fetch = FetchType.LAZY)
    @BatchSize(size = 100)
    val commentEntities: MutableSet<CommentEntity> = mutableSetOf()

    @OneToMany(mappedBy = "feedEntity", cascade = [CascadeType.REMOVE], orphanRemoval = true)
    @BatchSize(size = 100)
    val feedImageMappingEntities: MutableSet<FeedImageMappingEntity> = mutableSetOf()

    fun add(feedImageMappingEntities: List<FeedImageMappingEntity>?) {
        if (feedImageMappingEntities == null) {
            return
        }

        feedImageMappingEntities.forEach { it.feedEntity = this }
        this.feedImageMappingEntities.addAll(feedImageMappingEntities)
    }

    fun getImageUris(): List<String> =
        feedImageMappingEntities.mapNotNull { it.objectStorageEntity?.fileUri }
}
