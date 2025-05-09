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
)
