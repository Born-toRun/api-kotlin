package kr.kro.btr.domain.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "marathon")
class MarathonEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    var title: String? = null,
    var owner: String? = null,
    var email: String? = null,
    var schedule: String? = null,
    var contact: String? = null,
    var course: String? = null,
    var location: String? = null,
    var venue: String? = null,
    var host: String? = null,
    var duration: String? = null,
    var homepage: String? = null,
    var venueDetail: String? = null,
    var remark: String? = null,
    var registeredAt: LocalDateTime = LocalDateTime.now(),

    @OneToMany(mappedBy = "marathonEntity", cascade = [CascadeType.REMOVE])
    var marathonBookmarkEntities: Set<MarathonBookmarkEntity> = HashSet()
)
