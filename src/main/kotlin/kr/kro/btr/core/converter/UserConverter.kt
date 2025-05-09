package kr.kro.btr.core.converter

import kr.kro.btr.adapter.`in`.web.payload.ModifyUserRequest
import kr.kro.btr.adapter.`in`.web.payload.ModifyUserResponse
import kr.kro.btr.adapter.`in`.web.payload.SignUpRequest
import kr.kro.btr.adapter.`in`.web.payload.UserDetailResponse
import kr.kro.btr.domain.entity.UserEntity
import kr.kro.btr.domain.port.model.BornToRunUser
import kr.kro.btr.domain.port.model.CreateUserCommand
import kr.kro.btr.domain.port.model.ModifyUserCommand
import kr.kro.btr.domain.port.model.SignUpCommand
import kr.kro.btr.infrastructure.model.CreateUserQuery
import kr.kro.btr.infrastructure.model.ModifyUserQuery
import kr.kro.btr.infrastructure.model.SignUpUserQuery
import org.springframework.stereotype.Component

@Component
class UserConverter {

    fun map(source: BornToRunUser): UserDetailResponse {
        return UserDetailResponse(
            userId = source.userId,
            userName = source.userName,
            crewName = source.crewName,
            profileImageUri = source.profileImageUri,
            isAdmin = source.isAdmin,
            isManager = source.isManager,
            yellowCardQty = source.yellowCardQty,
            instagramId = source.instagramId,
            isInstagramIdPublic = source.isInstagramIdPublic
        )
    }

    fun mapToModifyUserResponse(source: BornToRunUser): ModifyUserResponse {
        return ModifyUserResponse(
            userName = source.userName,
            crewName = source.crewName,
            instagramId = source.instagramId,
            profileImageUri = source.profileImageUri
        )
    }

    fun map(source: SignUpRequest, userId: Long): SignUpCommand {
        return SignUpCommand(
            userId = userId,
            userName = source.userName,
            crewId = source.crewId,
            instagramId = source.instagramId,
        )
    }

    fun map(source: ModifyUserRequest, userId: Long): ModifyUserCommand {
        return ModifyUserCommand(
            userId = userId,
            profileImageId = source.profileImageId,
            instagramId = source.instagramId
        )
    }

    fun map(source: SignUpCommand): SignUpUserQuery {
        return SignUpUserQuery(
            userId = source.userId,
            userName = source.userName,
            crewId = source.crewId,
            instagramId = source.instagramId
        )
    }

    fun map(source: ModifyUserCommand): ModifyUserQuery {
        return ModifyUserQuery(
            userId = source.userId,
            profileImageId = source.profileImageId,
            instagramId = source.instagramId,
        )
    }

    fun map(source: CreateUserCommand): CreateUserQuery {
        return CreateUserQuery(
            socialId = source.socialId,
            providerType = source.providerType,
            roleType = source.roleType
        )
    }

    fun map(source: CreateUserQuery): UserEntity {
        return UserEntity(
            socialId = source.socialId,
            providerType = source.providerType,
            roleType = source.roleType
        )
    }

    fun map(source: UserEntity): BornToRunUser {
        return BornToRunUser(
            userId = source.id,
            socialId = source.socialId,
            providerType = source.providerType,
//            refreshToken = source.userRefreshTokenEntity?.refreshToken,
            roleType = source.roleType,
            userName = source.name,
            crewId = source.crewId,
            crewName = source.crewEntity?.name,
            instagramId = source.instagramId,
            imageId = source.imageId,
            profileImageUri = source.getProfileImageUri(),
            lastLoginAt = source.lastLoginAt,
            isAdmin = source.getIsAdmin(),
            isManager = source.getIsManager(),
            yellowCardQty = source.yellowCardQty,
            isInstagramIdPublic = source.userPrivacyEntity?.isInstagramIdPublic,
        )
    }
}
