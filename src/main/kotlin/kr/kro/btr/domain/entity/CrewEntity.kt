package kr.kro.btr.domain.entity

import jakarta.persistence.*
import org.hibernate.annotations.BatchSize

@Entity
@Table(name = "crew")
class CrewEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    var name: String,
    var contents: String,
    var sns: String? = null,
    var region: String,
    var imageId: Long? = null,
    var logoId: Long? = null,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "imageId", insertable = false, updatable = false)
    val imageEntity: ObjectStorageEntity? = null,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "logoId", insertable = false, updatable = false)
    val logoEntity: ObjectStorageEntity? = null
) {
    @OneToMany(mappedBy = "crewEntity", fetch = FetchType.LAZY)
    @BatchSize(size = 100)
    val userEntities: MutableSet<UserEntity> = mutableSetOf()

    fun modify(query: kr.kro.btr.domain.model.ModifyCrewQuery) {
        query.name.takeIf { it != name }?.let { name = it }
        query.contents.takeIf { it != contents }?.let { contents = it }
        query.sns?.takeIf { it != sns }?.let { sns = it }
        query.region.takeIf { it != region }?.let { region = it }
        query.imageId?.let { imageId = it }
        query.logoId?.let { logoId = it }
    }
}
