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
    val id: Long = 0,

    val userId: Long,
    val bucketName: String,
    var fileUri: String,
    val uploadAt: LocalDateTime = LocalDateTime.now(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    val userEntity: UserEntity? = null
)
