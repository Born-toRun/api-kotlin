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
    var id: Long = 0,

    var activityId: Long,
    var userId: Long,
    var updatedAt: LocalDateTime = LocalDateTime.now(),
    var registeredAt: LocalDateTime = LocalDateTime.now(),
    var isAttendance: Boolean = true,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id", insertable = false, updatable = false)
    var activityEntity: ActivityEntity? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    var userEntity: UserEntity? = null
) {
    fun attendance() {
        this.isAttendance = true
    }
}
