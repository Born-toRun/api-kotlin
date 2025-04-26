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
    var id: Long = 0,

    var socialId: String,

    @Enumerated(EnumType.STRING)
    var providerType: ProviderType,

    @Enumerated(EnumType.STRING)
    var roleType: RoleType = RoleType.GUEST,

    var name: String? = null,

    var crewId: Long? = null,

    var instagramId: String? = null,

    var lastLoginAt: LocalDateTime = LocalDateTime.now(),

    var imageId: Long? = null,

    var yellowCardQty: Int = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crewId", insertable = false, updatable = false)
    var crewEntity: CrewEntity? = null,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "imageId", insertable = false, updatable = false)
    var profileImageEntity: ObjectStorageEntity? = null,

    @OneToMany(mappedBy = "userEntity", cascade = [CascadeType.REMOVE])
    var objectStorageEntities: MutableSet<ObjectStorageEntity> = mutableSetOf(),

    @OneToOne(mappedBy = "userEntity", cascade = [CascadeType.REMOVE])
    var userRefreshTokenEntity: UserRefreshTokenEntity? = null,

    @OneToMany(mappedBy = "userEntity", cascade = [CascadeType.REMOVE])
    var feedEntities: MutableSet<FeedEntity> = mutableSetOf(),

    @OneToMany(mappedBy = "userEntity", cascade = [CascadeType.REMOVE])
    var activityEntities: MutableSet<ActivityEntity> = mutableSetOf(),

    @OneToMany(mappedBy = "userEntity", cascade = [CascadeType.REMOVE])
    var activityParticipationEntities: MutableSet<ActivityParticipationEntity> = mutableSetOf(),

    @OneToMany(mappedBy = "userEntity", cascade = [CascadeType.REMOVE])
    var commentEntities: MutableSet<CommentEntity> = mutableSetOf(),

    @OneToMany(mappedBy = "userEntity", cascade = [CascadeType.REMOVE])
    var marathonBookmarkEntities: MutableSet<MarathonBookmarkEntity> = mutableSetOf(),

    @OneToMany(mappedBy = "userEntity", cascade = [CascadeType.REMOVE])
    var recommendationEntities: MutableSet<RecommendationEntity> = mutableSetOf(),

    @OneToOne(mappedBy = "userEntity", cascade = [CascadeType.REMOVE])
    var userPrivacyEntity: UserPrivacyEntity? = null
) {

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
