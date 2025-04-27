package kr.kro.btr.domain.entity

import java.io.Serializable

data class YellowCardMultiKey(
    val targetUserId: Long = 0,
    val sourceUserId: Long = 0
) : Serializable
