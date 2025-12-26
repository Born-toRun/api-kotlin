package kr.kro.btr.domain.entity

import jakarta.persistence.*
import kr.kro.btr.domain.constant.ProviderType
import kr.kro.btr.domain.constant.RoleType
import org.hibernate.annotations.BatchSize
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import java.time.LocalDateTime

@Entity
@Table(name = "`user`")
@DynamicInsert
@DynamicUpdate
class UserEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val socialId: String,
    @Enumerated(EnumType.STRING)
    val providerType: ProviderType,
    @Enumerated(EnumType.STRING)
    var roleType: RoleType = RoleType.GUEST,
    var name: String? = null,
    var crewId: Long? = null,
    var managedCrewId: Long? = null,
    var instagramId: String? = null,
    var lastLoginAt: LocalDateTime = LocalDateTime.now(),
    var imageId: Long? = null,
    val yellowCardQty: Int = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crewId", insertable = false, updatable = false)
    val crewEntity: CrewEntity? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "managedCrewId", insertable = false, updatable = false)
    val managedCrewEntity: CrewEntity? = null,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "imageId", insertable = false, updatable = false)
    val profileImageEntity: ObjectStorageEntity? = null,

    @OneToOne(mappedBy = "userEntity", cascade = [CascadeType.REMOVE], fetch = FetchType.LAZY)
    var userPrivacyEntity: UserPrivacyEntity? = null
) {
    @OneToMany(mappedBy = "userEntity", cascade = [CascadeType.REMOVE], fetch = FetchType.LAZY)
    @BatchSize(size = 100)
    val objectStorageEntities: MutableSet<ObjectStorageEntity> = mutableSetOf()

    @OneToMany(mappedBy = "userEntity", cascade = [CascadeType.REMOVE], fetch = FetchType.LAZY)
    @BatchSize(size = 100)
    val feedEntities: MutableSet<FeedEntity> = mutableSetOf()

    @OneToMany(mappedBy = "userEntity", cascade = [CascadeType.REMOVE], fetch = FetchType.LAZY)
    @BatchSize(size = 100)
    val activityEntities: MutableSet<ActivityEntity> = mutableSetOf()

    @OneToMany(mappedBy = "userEntity", cascade = [CascadeType.REMOVE], fetch = FetchType.LAZY)
    @BatchSize(size = 100)
    val activityParticipationEntities: MutableSet<ActivityParticipationEntity> = mutableSetOf()

    @OneToMany(mappedBy = "userEntity", cascade = [CascadeType.REMOVE], fetch = FetchType.LAZY)
    @BatchSize(size = 100)
    val commentEntities: MutableSet<CommentEntity> = mutableSetOf()

    @OneToMany(mappedBy = "userEntity", cascade = [CascadeType.REMOVE], fetch = FetchType.LAZY)
    @BatchSize(size = 100)
    val marathonBookmarkEntities: MutableSet<MarathonBookmarkEntity> = mutableSetOf()

    @OneToMany(mappedBy = "userEntity", cascade = [CascadeType.REMOVE], fetch = FetchType.LAZY)
    @BatchSize(size = 100)
    val recommendationEntities: MutableSet<RecommendationEntity> = mutableSetOf()

    fun getProfileImageUri(): String? {
        return if (profileImageEntity == null || profileImageEntity?.id == 0L) {
            null
        } else {
            profileImageEntity?.fileUri
        }
    }

    fun getIsAdmin(): Boolean = roleType == RoleType.ADMIN

    fun getIsManager(): Boolean = roleType == RoleType.MANAGER

    fun modify(instagramId: String?, profileImageId: Long?) {
        profileImageId?.let {
            this.imageId = if (it == 0L) null else it
        }

        instagramId?.takeIf { it.isNotBlank() }?.let {
            this.instagramId = it
        }
    }

    fun modify(userName: String?, crewId: Long?, instagramId: String?, roleType: RoleType) {
        crewId?.takeIf { it != 0L }?.let {
            this.crewId = it
        }

        instagramId?.takeIf { it.isNotBlank() }?.let {
            this.instagramId = it
        }

        userName?.takeIf { it.isNotBlank() }?.let {
            this.name = it
        }

        roleType.let {
            this.roleType = it
        }
    }

    fun add(userPrivacyEntity: UserPrivacyEntity) {
        this.userPrivacyEntity = userPrivacyEntity
        userPrivacyEntity.userEntity = this
    }
}
