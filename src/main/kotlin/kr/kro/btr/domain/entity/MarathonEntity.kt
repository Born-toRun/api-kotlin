package kr.kro.btr.domain.entity

import jakarta.persistence.*
import org.hibernate.annotations.BatchSize
import java.time.LocalDateTime

@Entity
@Table(name = "marathon")
class MarathonEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val title: String? = null,
    val owner: String? = null,
    val email: String? = null,
    val schedule: String? = null,
    val contact: String? = null,
    val course: String? = null,
    val location: String? = null,
    val venue: String? = null,
    val host: String? = null,
    val duration: String? = null,
    val homepage: String? = null,
    val venueDetail: String? = null,
    val remark: String? = null,
    val registeredAt: LocalDateTime = LocalDateTime.now()
) {
    @OneToMany(mappedBy = "marathonEntity", cascade = [CascadeType.REMOVE], fetch = FetchType.LAZY)
    @BatchSize(size = 100)
    val marathonBookmarkEntities: MutableSet<MarathonBookmarkEntity> = mutableSetOf()
}
