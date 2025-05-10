package kr.kro.btr.domain.entity

import jakarta.persistence.*
import org.hibernate.annotations.DynamicInsert
import java.time.LocalDateTime

@Entity
@Table(name = "activity_participation")
@DynamicInsert
class ActivityParticipationEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val activityId: Long,
    val userId: Long,
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    val registeredAt: LocalDateTime = LocalDateTime.now(),
    var isAttendance: Boolean = true,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activityId", insertable = false, updatable = false)
    val activityEntity: ActivityEntity? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    val userEntity: UserEntity? = null
) {
    fun attendance() {
        this.isAttendance = true
    }
}
