package kr.kro.btr.domain.entity

import jakarta.persistence.*
import kr.kro.btr.core.event.MinioRemoveListener
import org.hibernate.annotations.DynamicInsert
import java.time.LocalDateTime

@Entity
@Table(name = "object_storage")
@DynamicInsert
@EntityListeners(MinioRemoveListener::class)
class ObjectStorageEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    var userId: Long,
    var bucketName: String,
    var fileUri: String,
    var uploadAt: LocalDateTime = LocalDateTime.now(),

    @OneToMany(mappedBy = "objectStorageEntity", cascade = [CascadeType.REMOVE])
    var feedImageMappingEntities: MutableSet<FeedImageMappingEntity> = HashSet(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    var userEntity: UserEntity? = null,

    @OneToOne(mappedBy = "imageEntity", cascade = [CascadeType.REMOVE])
    var crewImageEntity: CrewEntity? = null,

    @OneToOne(mappedBy = "logoEntity", cascade = [CascadeType.REMOVE])
    var crewLogoEntity: CrewEntity? = null
)

