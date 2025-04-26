package kr.kro.btr.domain.entity

import jakarta.persistence.*
import kr.kro.btr.domain.constant.FeedAccessLevel
import kr.kro.btr.domain.constant.FeedCategory
import kr.kro.btr.domain.constant.RecommendationType
import kr.kro.btr.infrastructure.model.ModifyFeedQuery
import org.hibernate.annotations.DynamicInsert
import java.time.LocalDateTime

@Entity
@Table(name = "feed")
@DynamicInsert
class FeedEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,
    var userId: Long = 0,
    var contents: String,
    @Enumerated(EnumType.STRING)
    var category: FeedCategory,
    @Enumerated(EnumType.STRING)
    var accessLevel: FeedAccessLevel,
    var viewQty: Int = 0,
    var registeredAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime = LocalDateTime.now(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    var userEntity: UserEntity? = null,

    @OneToMany(mappedBy = "feedEntity", cascade = [CascadeType.REMOVE], orphanRemoval = true)
    var commentEntities: MutableSet<CommentEntity> = mutableSetOf(),

    @OneToMany(mappedBy = "feedEntity", cascade = [CascadeType.REMOVE], orphanRemoval = true)
    var feedImageMappingEntities: MutableSet<FeedImageMappingEntity> = mutableSetOf(),

    @OneToMany(mappedBy = "feedEntity", cascade = [CascadeType.REMOVE], orphanRemoval = true)
    var recommendationEntities: MutableSet<RecommendationEntity> = mutableSetOf()
) {

    fun add(feedImageMappingEntities: List<FeedImageMappingEntity>) {
        feedImageMappingEntities.forEach { it.feedEntity = this }
        this.feedImageMappingEntities.addAll(feedImageMappingEntities)
    }

    fun modify(feedImageMappingEntities: List<FeedImageMappingEntity>) {
        feedImageMappingEntities.forEach { it.feedEntity = this }
        this.feedImageMappingEntities.clear()
        this.feedImageMappingEntities.addAll(feedImageMappingEntities)
    }

    fun modify(query: ModifyFeedQuery) {
        this.accessLevel = query.accessLevel
        this.contents = query.contents
        this.category = query.category

        val entities = query.imageIds.map {
            FeedImageMappingEntity(imageId=it).apply { feedEntity = this@FeedEntity }
        }

        add(entities)
    }

    fun getRecommendationQty(): Int =
        recommendationEntities.count { it.recommendationType == RecommendationType.FEED }

    fun getCommentQty(): Long = commentEntities.size.toLong()

    fun getImageUris(): List<String> =
        feedImageMappingEntities.mapNotNull { it.objectStorageEntity?.fileUri }

    fun hasMyComment(myUserId: Long): Boolean =
        commentEntities.any { it.userId == myUserId }

    fun hasMyRecommendation(myUserId: Long): Boolean =
        recommendationEntities.any { it.userId == myUserId }
}
