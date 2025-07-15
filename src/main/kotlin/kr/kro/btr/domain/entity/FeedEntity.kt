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
    @OneToMany(mappedBy = "feedEntity", cascade = [CascadeType.REMOVE], orphanRemoval = true)
    val commentEntities: MutableSet<CommentEntity> = mutableSetOf()

    @OneToMany(mappedBy = "feedEntity", cascade = [CascadeType.REMOVE], orphanRemoval = true)
    val feedImageMappingEntities: MutableSet<FeedImageMappingEntity> = mutableSetOf()

    @OneToMany(mappedBy = "feedEntity", cascade = [CascadeType.REMOVE], orphanRemoval = true)
    val recommendationEntities: MutableSet<RecommendationEntity> = mutableSetOf()

    fun add(feedImageMappingEntities: List<FeedImageMappingEntity>?) {
        if (feedImageMappingEntities == null) {
            return
        }

        feedImageMappingEntities.forEach { it.feedEntity = this }
        this.feedImageMappingEntities.addAll(feedImageMappingEntities)
    }

    fun modify(feedImageMappingEntities: List<FeedImageMappingEntity>?) {
        if (feedImageMappingEntities == null) {
            return
        }

        feedImageMappingEntities.forEach { it.feedEntity = this }
        this.feedImageMappingEntities.clear()
        this.feedImageMappingEntities.addAll(feedImageMappingEntities)
    }

    fun modify(query: ModifyFeedQuery) {
        this.accessLevel = query.accessLevel
        this.contents = query.contents
        this.category = query.category

        val entities = query.imageIds?.map {
            FeedImageMappingEntity(imageId=it).apply { feedEntity = this@FeedEntity }
        }

        add(entities)
    }

    fun getRecommendationQty(): Int =
        recommendationEntities.count { it.recommendationType == RecommendationType.FEED }

    fun getCommentQty(): Int = commentEntities.size.toInt()

    fun getImageUris(): List<String> =
        feedImageMappingEntities.mapNotNull { it.objectStorageEntity?.fileUri }

    fun hasMyComment(myUserId: Long): Boolean =
        commentEntities.any { it.userId == myUserId }

    fun hasMyRecommendation(myUserId: Long): Boolean =
        recommendationEntities.any { it.userId == myUserId }
}
