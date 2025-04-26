package kr.kro.btr.domain.entity

import java.io.Serializable

data class YellowCardMultiKey(
    var targetUserId: Long = 0,
    var sourceUserId: Long = 0
) : Serializable