package kr.kro.btr.domain.entity

import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import kr.kro.btr.domain.model.ModifyAnnounceQuery
import java.time.LocalDateTime

@Entity
@Table(name = "announce")
class AnnounceEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    var title: String,
    var contents: String,
    val userId: Long,
    var postedAt: LocalDateTime,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    val userEntity: UserEntity? = null
) {

    fun modify(query: ModifyAnnounceQuery) {
        query.title.takeIf { it != title }?.let { title = it }
        query.contents.takeIf { it != contents }?.let { contents = it }
        query.postedAt.takeIf { it != postedAt }?.let { postedAt = it }
    }
}
