package kr.kro.btr.domain.entity

import jakarta.persistence.*
import kr.kro.btr.domain.constant.FeedbackType
import org.hibernate.annotations.DynamicInsert
import java.time.LocalDateTime

@Entity
@Table(name = "feedback")
@DynamicInsert
class FeedbackEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val userId: Long,

    @Column(length = 2000)
    val content: String,

    @Enumerated(EnumType.STRING)
    val feedbackType: FeedbackType,

    val registeredAt: LocalDateTime = LocalDateTime.now(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    val userEntity: UserEntity? = null
)
