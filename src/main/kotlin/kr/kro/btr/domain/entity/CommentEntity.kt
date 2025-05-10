package kr.kro.btr.domain.entity

import jakarta.persistence.*
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
    val id: Long = 0,

    val parentId: Long? = null,
    val userId: Long,
    val feedId: Long? = null,
    var contents: String,
    val registeredAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    val userEntity: UserEntity? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feedId", insertable = false, updatable = false)
    val feedEntity: FeedEntity? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parentId", insertable = false, updatable = false)
    var parent: CommentEntity? = null
) {
    @OneToMany(mappedBy = "commentEntity", cascade = [CascadeType.REMOVE])
    val recommendationEntities: MutableSet<RecommendationEntity> = mutableSetOf()

    @OneToMany(mappedBy = "parent", cascade = [CascadeType.REMOVE], orphanRemoval = true)
    val child: MutableSet<CommentEntity> = mutableSetOf()

    fun isRootComment(): Boolean {
        return parentId == null
    }
}
