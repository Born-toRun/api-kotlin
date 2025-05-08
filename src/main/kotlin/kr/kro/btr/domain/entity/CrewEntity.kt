package kr.kro.btr.domain.entity

import jakarta.persistence.*

@Entity
@Table(name = "crew")
class CrewEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val name: String,
    val contents: String,
    val sns: String? = null,
    val region: String,
    val imageId: Long? = null,
    val logoId: Long? = null,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "imageId", insertable = false, updatable = false)
    val imageEntity: ObjectStorageEntity? = null,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "logoId", insertable = false, updatable = false)
    val logoEntity: ObjectStorageEntity? = null
) {
    @OneToMany(mappedBy = "crewEntity")
    val userEntities: MutableSet<UserEntity> = mutableSetOf()
}
