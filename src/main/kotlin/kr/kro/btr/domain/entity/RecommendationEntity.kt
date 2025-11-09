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
    val id: Long = 0,
    val contentId: Long,
    val userId: Long,
    @Enumerated(EnumType.STRING)
    val recommendationType: RecommendationType,
    val registeredAt: LocalDateTime = LocalDateTime.now(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    val userEntity: UserEntity? = null
)
