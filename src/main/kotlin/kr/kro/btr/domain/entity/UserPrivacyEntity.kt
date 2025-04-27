package kr.kro.btr.domain.entity

import jakarta.persistence.*
import org.hibernate.annotations.DynamicInsert

@Entity
@Table(name = "user_privacy")
@DynamicInsert
class UserPrivacyEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val userId: Long,
    var isInstagramIdPublic: Boolean = false,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    var userEntity: UserEntity? = null
) {
    fun change(isInstagramIdPublic: Boolean) {
        this.isInstagramIdPublic = isInstagramIdPublic
    }
}
