package kr.kro.btr.domain.entity

import jakarta.persistence.*
import java.io.Serializable

@Entity
@Table(name = "crew")
class CrewEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,
    var name: String,
    var contents: String,
    var sns: String? = null,
    var region: String,
    var imageId: Long? = null,
    var logoId: Long? = null,

    @OneToMany(mappedBy = "crewEntity")
    var userEntities: Set<UserEntity> = emptySet(),

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id", insertable = false, updatable = false)
    var imageEntity: ObjectStorageEntity? = null,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "logoId", insertable = false, updatable = false)
    var logoEntity: ObjectStorageEntity? = null

) : Serializable
