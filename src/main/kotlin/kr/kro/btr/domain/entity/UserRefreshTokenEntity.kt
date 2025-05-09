package kr.kro.btr.domain.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table

@Entity
@Table(name = "user_refresh_token")
class UserRefreshTokenEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    val id: Long = 0,
    val userId: Long,
    var refreshToken: String,

//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "userId", insertable = false, updatable = false)
//    @JsonIgnore
//    var userEntity: UserEntity? = null
) {
    fun add(userEntity: UserEntity) {
//        userEntity.userRefreshTokenEntity = this
//        this.userEntity = userEntity
    }
}

