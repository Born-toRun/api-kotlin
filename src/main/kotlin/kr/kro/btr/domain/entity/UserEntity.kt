package kr.kro.btr.domain.entity

import jakarta.persistence.*
import kr.kro.btr.domain.constant.ProviderType
import kr.kro.btr.domain.constant.RoleType
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import org.springframework.util.StringUtils
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
    val roleType: RoleType = RoleType.GUEST,
    var name: String? = null,
    var crewId: Long? = null,
    var instagramId: String? = null,
    var lastLoginAt: LocalDateTime = LocalDateTime.now(),
    var imageId: Long? = null,
    val yellowCardQty: Int = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crewId", insertable = false, updatable = false)
    val crewEntity: CrewEntity? = null,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "imageId", insertable = false, updatable = false)
    val profileImageEntity: ObjectStorageEntity? = null,

    @OneToOne(mappedBy = "userEntity", cascade = [CascadeType.REMOVE])
    var userRefreshTokenEntity: UserRefreshTokenEntity? = null,

    @OneToOne(mappedBy = "userEntity", cascade = [CascadeType.REMOVE])
    var userPrivacyEntity: UserPrivacyEntity? = null
) {
    @OneToMany(mappedBy = "userEntity", cascade = [CascadeType.REMOVE])
    val objectStorageEntities: MutableSet<ObjectStorageEntity> = mutableSetOf()

    @OneToMany(mappedBy = "userEntity", cascade = [CascadeType.REMOVE])
    val feedEntities: MutableSet<FeedEntity> = mutableSetOf()

    @OneToMany(mappedBy = "userEntity", cascade = [CascadeType.REMOVE])
    val activityEntities: MutableSet<ActivityEntity> = mutableSetOf()

    @OneToMany(mappedBy = "userEntity", cascade = [CascadeType.REMOVE])
    val activityParticipationEntities: MutableSet<ActivityParticipationEntity> = mutableSetOf()

    @OneToMany(mappedBy = "userEntity", cascade = [CascadeType.REMOVE])
    val commentEntities: MutableSet<CommentEntity> = mutableSetOf()

    @OneToMany(mappedBy = "userEntity", cascade = [CascadeType.REMOVE])
    val marathonBookmarkEntities: MutableSet<MarathonBookmarkEntity> = mutableSetOf()

    @OneToMany(mappedBy = "userEntity", cascade = [CascadeType.REMOVE])
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
        if (profileImageId != 0L) {
            this.imageId = profileImageId
        }
        if (StringUtils.hasLength(instagramId)) {
            this.instagramId = instagramId
        }
    }

    fun modify(userName: String?, crewId: Long?, instagramId: String?) {
        if (crewId != null && crewId != 0L) {
            this.crewId = crewId
        }
        if (StringUtils.hasLength(instagramId)) {
            this.instagramId = instagramId
        }
        if (StringUtils.hasLength(userName)) {
            this.name = userName
        }
    }

    fun add(userPrivacyEntity: UserPrivacyEntity) {
        this.userPrivacyEntity = userPrivacyEntity
        userPrivacyEntity.userEntity = this
    }
}
