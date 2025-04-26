package kr.kro.btr.domain.entity

import jakarta.persistence.*
import kr.kro.btr.domain.constant.RecommendationType
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import java.time.LocalDateTime

@Entity
@Table(name = "comment")
@DynamicInsert
@DynamicUpdate
class CommentEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    var parentId: Long? = null,
    var userId: Long,
    var feedId: Long? = null,
    var contents: String,
    var registeredAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime = LocalDateTime.now(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    var userEntity: UserEntity? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feedId", insertable = false, updatable = false)
    var feedEntity: FeedEntity? = null,

    @OneToMany(mappedBy = "commentEntity", cascade = [CascadeType.REMOVE])
    var recommendationEntities: MutableSet<RecommendationEntity> = HashSet(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parentId", insertable = false, updatable = false)
    var parent: CommentEntity? = null,

    @OneToMany(mappedBy = "parent", cascade = [CascadeType.ALL], orphanRemoval = true)
    var child: MutableList<CommentEntity> = ArrayList()
) {
    fun addParent(parent: CommentEntity?) {
        this.parent = parent
        parent?.child?.add(this)
    }

    fun getRecommendationQty(): Long {
        return recommendationEntities
            .filter { it.recommendationType == RecommendationType.FEED }
            .count()
            .toLong()
    }

    fun isRootComment(): Boolean {
        return parentId == null
    }
}