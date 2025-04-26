package kr.kro.btr.domain.entity
import jakarta.persistence.*
import kr.kro.btr.domain.constant.RecommendationType
import org.hibernate.annotations.DynamicInsert
import java.time.LocalDateTime

@Entity
@Table(name = "recommendation")
@DynamicInsert
class RecommendationEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    var contentId: Long,

    var userId: Long,

    @Enumerated(EnumType.STRING)
    var recommendationType: RecommendationType,

    var registeredAt: LocalDateTime = LocalDateTime.now(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    var userEntity: UserEntity? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contentId", insertable = false, updatable = false)
    var feedEntity: FeedEntity? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contentId", insertable = false, updatable = false)
    var commentEntity: CommentEntity? = null
) {
    @Transient
    fun getContentEntity(): Any? {
        return when (recommendationType) {
            RecommendationType.FEED -> feedEntity
            RecommendationType.COMMENT -> commentEntity
            else -> null
        }
    }
}
