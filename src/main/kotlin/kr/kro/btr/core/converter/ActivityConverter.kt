package kr.kro.btr.core.converter

import kr.kro.btr.adapter.`in`.web.payload.AttendanceActivityRequest
import kr.kro.btr.adapter.`in`.web.payload.CreateActivityRequest
import kr.kro.btr.adapter.`in`.web.payload.ModifyActivityRequest
import kr.kro.btr.adapter.`in`.web.payload.OpenActivityResponse
import kr.kro.btr.adapter.`in`.web.payload.ParticipationActivityResponse
import kr.kro.btr.adapter.`in`.web.payload.SearchActivityDetailResponse
import kr.kro.btr.adapter.`in`.web.payload.SearchActivityResponse
import kr.kro.btr.adapter.`in`.web.payload.SearchActivityResponse.Host
import kr.kro.btr.adapter.`in`.web.payload.SearchAllActivityRequest
import kr.kro.btr.domain.constant.ActivityRecruitmentType
import kr.kro.btr.domain.entity.ActivityEntity
import kr.kro.btr.domain.entity.ActivityParticipationEntity
import kr.kro.btr.domain.entity.UserEntity
import kr.kro.btr.domain.port.model.ActivityResult
import kr.kro.btr.domain.port.model.AttendanceActivityCommand
import kr.kro.btr.domain.port.model.CreateActivityCommand
import kr.kro.btr.domain.port.model.ModifyActivityCommand
import kr.kro.btr.domain.port.model.ParticipantResult
import kr.kro.btr.domain.port.model.ParticipateActivityCommand
import kr.kro.btr.domain.port.model.SearchAllActivityCommand
import kr.kro.btr.infrastructure.model.AttendanceActivityQuery
import kr.kro.btr.infrastructure.model.CreateActivityQuery
import kr.kro.btr.infrastructure.model.ModifyActivityQuery
import kr.kro.btr.infrastructure.model.ParticipateActivityQuery
import kr.kro.btr.infrastructure.model.SearchAllActivityQuery
import kr.kro.btr.support.TokenDetail
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import kotlin.Long
import kotlin.collections.List

@Component
class ActivityConverter {

    fun map(source: List<ActivityResult>): SearchActivityResponse {
        val activities = source.map { activityResult ->
            SearchActivityResponse.Activity(
                id = activityResult.id,
                title = activityResult.title,
                host = Host(
                    userId = activityResult.host.userId,
                    crewId = activityResult.host.crewId,
                    userProfileUri = activityResult.host.userProfileUri,
                    userName = activityResult.host.userName,
                    crewName = activityResult.host.crewName,
                    isManager = activityResult.host.isManager,
                    isAdmin = activityResult.host.isAdmin
                ),
                startAt = activityResult.startAt,
                course = activityResult.course,
                participantsLimit = activityResult.participantsLimit,
                participantsQty = activityResult.participantsQty,
                updatedAt = activityResult.updatedAt,
                registeredAt = activityResult.registeredAt,
                isOpen = activityResult.isOpen,
                recruitmentType = activityResult.recruitmentType
            )
        }
        return SearchActivityResponse(activities)
    }

    fun mapToSearchActivityDetailResponse(source: ActivityResult): SearchActivityDetailResponse {
        return SearchActivityDetailResponse(
            id = source.id,
            title = source.title,
            contents = source.contents,
            startAt = source.startAt,
            venue = source.venue,
            venueUrl = source.venueUrl,
            participantsLimit = source.participantsLimit,
            participantsQty = source.participantsQty,
            participationFee = source.participationFee,
            course = source.course,
            courseDetail = source.courseDetail,
            path = source.path,
            isOpen = source.isOpen,
            updatedAt = source.updatedAt,
            registeredAt = source.registeredAt,
            host = SearchActivityDetailResponse.Host(
                userId = source.host.userId,
                crewId = source.host.crewId,
                userProfileUri = source.host.userProfileUri,
                userName = source.host.userName,
                crewName = source.host.crewName,
                isManager = source.host.isManager,
                isAdmin = source.host.isAdmin
            )
        )
    }

    fun mapToOpenActivityResponse(source: ActivityResult): OpenActivityResponse {
        return OpenActivityResponse(
            activityId = source.id,
            attendanceCode = source.attendanceCode
        )
    }

    fun map(source: ParticipantResult): ParticipationActivityResponse {
        val participants = source.participants?.map { participant ->
            ParticipationActivityResponse.Person(
                participationId = participant.participationId,
                userId = participant.userId,
                userName = participant.userName,
                crewName = participant.crewName,
                userProfileUri = participant.userProfileUri
            )
        }
        return ParticipationActivityResponse(
            host = ParticipationActivityResponse.Person(
                userId = source.host.userId,
                userName = source.host.userName,
                crewName = source.host.crewName,
                userProfileUri = source.host.userProfileUri
            ),
            participants = participants
        )
    }

    fun map(source: CreateActivityRequest, my: TokenDetail): CreateActivityCommand {
        return CreateActivityCommand(
            title = source.title,
            contents = source.contents,
            startAt = source.startAt,
            venue = source.venue,
            venueUrl = source.venueUrl,
            participantsLimit = source.participantsLimit,
            participationFee = source.participationFee,
            course = source.course,
            courseDetail = source.courseDetail,
            path = source.path,
            myUserId = my.id
        )
    }

    fun map(source: ModifyActivityRequest, activityId: Long): ModifyActivityCommand {
        return ModifyActivityCommand(
            activityId = activityId,
            title = source.title,
            contents = source.contents,
            startAt = source.startAt,
            venue = source.venue,
            venueUrl = source.venueUrl,
            participantsLimit = source.participantsLimit,
            participationFee = source.participationFee,
            course = source.course,
            courseDetail = source.courseDetail,
            path = source.path,
        )
    }

    fun map(source: SearchAllActivityRequest, my: TokenDetail): SearchAllActivityCommand {
        return SearchAllActivityCommand(
            courses = source.courses,
            recruitmentType = source.recruitmentType,
            myCrewId = my.crewId,
            myUserId = my.id
        )
    }

    fun map(source: AttendanceActivityRequest, activityId: Long, myUserId: Long): AttendanceActivityCommand {
        return AttendanceActivityCommand(
            activityId = activityId,
            accessCode = source.accessCode,
            myUserId = myUserId,
        )
    }

    fun map(source: CreateActivityCommand): CreateActivityQuery {
        return CreateActivityQuery(
            title = source.title,
            contents = source.contents,
            startAt = source.startAt,
            venue = source.venue,
            venueUrl = source.venueUrl,
            participantsLimit = source.participantsLimit,
            participationFee = source.participationFee,
            course = source.course,
            courseDetail = source.courseDetail,
            path = source.path,
            myUserId = source.myUserId
        )
    }

    fun map(source: ModifyActivityCommand): ModifyActivityQuery {
        return ModifyActivityQuery(
            activityId = source.activityId,
            title = source.title,
            contents = source.contents,
            startAt = source.startAt,
            venue = source.venue,
            venueUrl = source.venueUrl,
            participantsLimit = source.participantsLimit,
            participationFee = source.participationFee,
            course = source.course,
            courseDetail = source.courseDetail,
            path = source.path
        )
    }

    fun map(source: SearchAllActivityCommand): SearchAllActivityQuery {
        return SearchAllActivityQuery(
            courses = source.courses,
            recruitmentType = source.recruitmentType,
            myCrewId = source.myCrewId,
            myUserId = source.myUserId
        )
    }

    fun map(source: List<ActivityEntity>, userId: Long): List<ActivityResult> {
        return source.map { activityEntity ->
            ActivityResult(
                id = activityEntity.id,
                title = activityEntity.title,
                contents = activityEntity.contents,
                startAt = activityEntity.startAt,
                venue = activityEntity.venue,
                venueUrl = activityEntity.venueUrl,
                participantsLimit = activityEntity.participantsLimit,
                participantsQty = activityEntity.activityParticipationEntities.size,
                participationFee = activityEntity.participationFee,
                course = activityEntity.course,
                courseDetail = activityEntity.courseDetail,
                path = activityEntity.path,
                isOpen = activityEntity.isOpen,
                updatedAt = activityEntity.updatedAt,
                registeredAt = activityEntity.registeredAt,
                recruitmentType = convertRecruitmentType(activityEntity, userId),
                host = map(activityEntity)
            )
        }
    }

    fun map(source: ActivityEntity, userId: Long): ActivityResult {
        return ActivityResult(
            id = source.id,
            title = source.title,
            contents = source.contents,
            startAt = source.startAt,
            venue = source.venue,
            venueUrl = source.venueUrl,
            participantsLimit = source.participantsLimit,
            participantsQty = source.activityParticipationEntities.size,
            participationFee = source.participationFee,
            course = source.course,
            courseDetail = source.courseDetail,
            path = source.path,
            isOpen = source.isOpen,
            updatedAt = source.updatedAt,
            registeredAt = source.registeredAt,
            recruitmentType = convertRecruitmentType(source, userId),
            host = map(source)
        )
    }

    fun map(source: ActivityEntity, accessCode: Int): ActivityResult {
        return ActivityResult(
            id = source.id,
            title = source.title,
            contents = source.contents,
            startAt = source.startAt,
            venue = source.venue,
            venueUrl = source.venueUrl,
            participantsLimit = source.participantsLimit,
            participantsQty = source.activityParticipationEntities.size,
            participationFee = source.participationFee,
            course = source.course,
            courseDetail = source.courseDetail,
            path = source.path,
            attendanceCode = accessCode,
            isOpen = source.isOpen,
            updatedAt = source.updatedAt,
            registeredAt = source.registeredAt,
            host = map(source)
        )
    }

    fun map(source: AttendanceActivityCommand): AttendanceActivityQuery {
        return AttendanceActivityQuery(
            activityId = source.activityId,
            accessCode = source.accessCode,
            myUserId = source.myUserId
        )
    }

    fun map(source: ParticipateActivityCommand): ParticipateActivityQuery {
        return ParticipateActivityQuery(
            activityId = source.activityId,
            myUserId = source.myUserId
        )
    }

    fun map(source: List<ActivityParticipationEntity>): ParticipantResult{
        val host = source.first().activityEntity!!.userEntity!!
        return ParticipantResult(
            host = map(host),
            participants = mapToParticipant(source)
        )
    }

    fun map(source: ParticipateActivityQuery): ActivityParticipationEntity {
        return ActivityParticipationEntity(
            activityId = source.activityId,
            userId = source.myUserId
        )
    }

    fun map(source: CreateActivityQuery): ActivityEntity {
        return ActivityEntity(
            title = source.title,
            contents = source.contents,
            startAt = source.startAt,
            venue = source.venue,
            venueUrl = source.venueUrl,
            participantsLimit = source.participantsLimit,
            participationFee = source.participationFee,
            course = source.course,
            courseDetail = source.courseDetail,
            path = source.path,
            userId = source.myUserId,
        )
    }

    fun mapToParticipant(source: List<ActivityParticipationEntity>): List<ParticipantResult.Participant> {
        return source.map { activityParticipationEntity ->
            val userEntity = activityParticipationEntity.userEntity!!

            ParticipantResult.Participant(
                participationId = activityParticipationEntity.id,
                userId = userEntity.id,
                userName = userEntity.name!!,
                crewName = userEntity.crewEntity!!.name,
                userProfileUri = userEntity.getProfileImageUri()

            )
        }
    }

    fun map(source: UserEntity): ParticipantResult.Participant {
        return ParticipantResult.Participant(
            userId = source.id,
            userName = source.name!!,
            crewName = source.crewEntity!!.name,
            userProfileUri = source.getProfileImageUri()
        )
    }

    fun mapToParticipantResult(source: ActivityEntity): ParticipantResult {
        return ParticipantResult(
            host = map(source.userEntity!!)
        )
    }

    fun map(source: ActivityEntity): ActivityResult.Host {
        val host = source.userEntity!!
        return ActivityResult.Host(
            userId = host.id,
            crewId = host.crewId!!,
            userProfileUri = host.getProfileImageUri(),
            userName = host.name!!,
            crewName = host.crewEntity!!.name,
            isManager = host.getIsManager(),
            isAdmin = host.getIsAdmin()
        )
    }

    fun convertRecruitmentType(source: ActivityEntity, myUserId: Long): ActivityRecruitmentType {
        if (source.activityParticipationEntities.any { it.userEntity?.id == myUserId }) {
            return ActivityRecruitmentType.ALREADY_PARTICIPATING
        }

        if (source.startAt.isBefore(LocalDateTime.now())) {
            return ActivityRecruitmentType.ENDED
        }

        if (source.participantsLimit >= source.activityParticipationEntities.size) {
            return ActivityRecruitmentType.CLOSED
        }

        return ActivityRecruitmentType.RECRUITING
    }
}
