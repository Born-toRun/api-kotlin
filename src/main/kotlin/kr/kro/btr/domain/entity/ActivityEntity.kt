package kr.kro.btr.domain.entity

import jakarta.persistence.*
import kr.kro.btr.domain.model.ModifyActivityQuery
import org.hibernate.annotations.BatchSize
import org.hibernate.annotations.DynamicInsert
import java.time.LocalDateTime

@Entity
@Table(name = "activity")
@DynamicInsert
class ActivityEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    var title: String,
    var contents: String,
    var startAt: LocalDateTime,
    var venue: String? = null,
    var venueUrl: String,
    var participantsLimit: Int = 0,
    var participationFee: Int = 0,
    var course: String? = null,
    var courseDetail: String? = null,
    var path: String? = null,
    val userId: Long,
    var isOpen: Boolean = false,
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    val registeredAt: LocalDateTime = LocalDateTime.now(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    val userEntity: UserEntity? = null
) {
    @OneToMany(mappedBy = "activityEntity", cascade = [CascadeType.REMOVE], fetch = FetchType.LAZY)
    @BatchSize(size = 100)
    val activityParticipationEntities: MutableSet<ActivityParticipationEntity> = mutableSetOf()

    @OneToMany(mappedBy = "activityEntity", cascade = [CascadeType.REMOVE], orphanRemoval = true)
    @BatchSize(size = 100)
    val activityImageMappingEntities: MutableSet<ActivityImageMappingEntity> = mutableSetOf()

    fun add(activityImageMappingEntities: List<ActivityImageMappingEntity>?) {
        if (activityImageMappingEntities == null) {
            return
        }

        activityImageMappingEntities.forEach { it.activityEntity = this }
        this.activityImageMappingEntities.addAll(activityImageMappingEntities)
    }

    fun open() {
        isOpen = true
    }

    fun modify(query: ModifyActivityQuery) {
        query.title.takeIf { it != title }?.let { title = it }
        query.contents.takeIf { it != contents }?.let { contents = it }
        query.startAt.takeIf { it != startAt }?.let { startAt = it }
        query.venue?.takeIf { it.isNotBlank() }?.let { venue = it }
        query.venueUrl.takeIf { it != venueUrl }?.let { venueUrl = it }
        query.participantsLimit.let { participantsLimit = it }
        query.participationFee.let { participationFee = it }
        query.course?.takeIf { it != course }?.let { course = it }
        query.courseDetail?.takeIf { it != courseDetail }?.let { courseDetail = it }
        query.path?.takeIf { it != path }?.let { path = it }
    }

    fun getImageUris(): List<String> =
        activityImageMappingEntities.mapNotNull { it.objectStorageEntity?.fileUri }
}
