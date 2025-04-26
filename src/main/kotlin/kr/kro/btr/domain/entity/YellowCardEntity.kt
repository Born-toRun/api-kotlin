package kr.kro.btr.domain.entity

import jakarta.persistence.*

@Entity
@Table(name = "yellow_card")
@IdClass(YellowCardMultiKey::class)
class YellowCardEntity(

    @Id
    var targetUserId: Long = 0,

    @Id
    var sourceUserId: Long = 0,

    var reason: String? = null,

    var basis: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "targetUserId", insertable = false, updatable = false)
    var targetUser: UserEntity? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sourceUserId", insertable = false, updatable = false)
    var sourceUser: UserEntity? = null
)
