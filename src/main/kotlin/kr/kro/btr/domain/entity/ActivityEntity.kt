package kr.kro.btr.domain.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import kr.kro.btr.infrastructure.model.ModifyActivityQuery
import org.hibernate.annotations.DynamicInsert
import java.time.LocalDateTime

@Entity
@Table(name = "activity")
@DynamicInsert
class ActivityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L
    var title: String = ""
    var contents: String = ""
    var startDate: LocalDateTime? = null
    var venue: String = ""
    var venueUrl: String = ""
    var participantsLimit: Int = 0
    var participationFee: Int = 0
    var course: String = ""
    var courseDetail: String = ""
    var path: String = ""
    var userId: Long? = null
    var isOpen: Boolean = false
    var updatedAt: LocalDateTime? = null
    var registeredAt: LocalDateTime? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    var userEntity: UserEntity? = null

    @OneToMany(mappedBy = "activityEntity", cascade = [CascadeType.REMOVE], fetch = FetchType.LAZY)
    var activityParticipationEntities: MutableSet<ActivityParticipationEntity> = mutableSetOf()

    fun open() {
        isOpen = true
    }

    fun modify(query: ModifyActivityQuery) {
        query.title.takeIf { it.isNotBlank() }?.let { title = it }
        query.contents.takeIf { it.isNotBlank() }?.let { contents = it }
        query.startAt.let { startDate = it }
        query.venue?.takeIf { it.isNotBlank() }?.let { venue = it }
        query.venueUrl.takeIf { it.isNotBlank() }?.let { venueUrl = it }
        query.participantsLimit.let { participantsLimit = it }
        query.participationFee.let { participationFee = it }
        query.course?.takeIf { it.isNotBlank() }?.let { course = it }
        query.courseDetail?.takeIf { it.isNotBlank() }?.let { courseDetail = it }
        query.path?.takeIf { it.isNotBlank() }?.let { path = it }
    }
}
