package kr.kro.btr.domain.entity

import jakarta.persistence.*

@Entity
@Table(name = "yellow_card")
@IdClass(YellowCardMultiKey::class)
class YellowCardEntity(

    @Id
    val targetUserId: Long = 0,
    @Id
    val sourceUserId: Long = 0,
    val reason: String? = null,
    val basis: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "targetUserId", insertable = false, updatable = false)
    val targetUser: UserEntity? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sourceUserId", insertable = false, updatable = false)
    val sourceUser: UserEntity? = null
)
