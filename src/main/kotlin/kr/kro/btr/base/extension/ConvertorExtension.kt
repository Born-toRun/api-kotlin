package kr.kro.btr.base.extension

import kr.kro.btr.adapter.`in`.web.payload.AttendanceActivityRequest
import kr.kro.btr.adapter.`in`.web.payload.CreateActivityRequest
import kr.kro.btr.adapter.`in`.web.payload.ModifyActivityRequest
import kr.kro.btr.adapter.`in`.web.payload.OpenActivityResponse
import kr.kro.btr.adapter.`in`.web.payload.ParticipationActivityResponse
import kr.kro.btr.adapter.`in`.web.payload.SearchActivityDetailResponse
import kr.kro.btr.adapter.`in`.web.payload.SearchActivityResponse
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
import java.time.LocalDateTime

fun List<ActivityResult>.toSearchActivityResponse(): SearchActivityResponse {

    val activities = this.map { activityResult ->
        SearchActivityResponse.Activity(
            id = activityResult.id,
            title = activityResult.title,
            host = SearchActivityResponse.Host(
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

fun ActivityResult.toSearchActivityDetailResponse(): SearchActivityDetailResponse {
    return SearchActivityDetailResponse(
        id = this.id,
        title = this.title,
        contents = this.contents,
        startAt = this.startAt,
        venue = this.venue,
        venueUrl = this.venueUrl,
        participantsLimit = this.participantsLimit,
        participantsQty = this.participantsQty,
        participationFee = this.participationFee,
        course = this.course,
        courseDetail = this.courseDetail,
        path = this.path,
        isOpen = this.isOpen,
        updatedAt = this.updatedAt,
        registeredAt = this.registeredAt,
        host = SearchActivityDetailResponse.Host(
            userId = this.host.userId,
            crewId = this.host.crewId,
            userProfileUri = this.host.userProfileUri,
            userName = this.host.userName,
            crewName = this.host.crewName,
            isManager = this.host.isManager,
            isAdmin = this.host.isAdmin
        )
    )
}

fun ActivityResult.toOpenActivityResponse(): OpenActivityResponse {
    return OpenActivityResponse(
        activityId = this.id,
        attendanceCode = this.attendanceCode
    )
}

fun ParticipantResult.toParticipationActivityResponse(): ParticipationActivityResponse {
    val participants = this.participants?.map { participant ->
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
            userId = this.host.userId,
            userName = this.host.userName,
            crewName = this.host.crewName,
            userProfileUri = this.host.userProfileUri
        ),
        participants = participants
    )
}

fun CreateActivityRequest.toCreateActivityCommand(my: TokenDetail): CreateActivityCommand {
    return CreateActivityCommand(
        title = this.title,
        contents = this.contents,
        startAt = this.startAt,
        venue = this.venue,
        venueUrl = this.venueUrl,
        participantsLimit = this.participantsLimit,
        participationFee = this.participationFee,
        course = this.course,
        courseDetail = this.courseDetail,
        path = this.path,
        myUserId = my.id
    )
}

fun ModifyActivityRequest.toModifyActivityCommand(activityId: Long): ModifyActivityCommand {
    return ModifyActivityCommand(
        activityId = activityId,
        title = this.title,
        contents = this.contents,
        startAt = this.startAt,
        venue = this.venue,
        venueUrl = this.venueUrl,
        participantsLimit = this.participantsLimit,
        participationFee = this.participationFee,
        course = this.course,
        courseDetail = this.courseDetail,
        path = this.path
    )
}

fun SearchAllActivityRequest.toSearchAllActivityCommand(my: TokenDetail): SearchAllActivityCommand {
    return SearchAllActivityCommand(
        courses = this.courses,
        recruitmentType = this.recruitmentType,
        myCrewId = my.crewId,
        myUserId = my.id
    )
}

fun AttendanceActivityRequest.toAttendanceActivityCommand(activityId: Long, myUserId: Long): AttendanceActivityCommand {
    return AttendanceActivityCommand(
        activityId = activityId,
        accessCode = this.accessCode,
        myUserId = myUserId
    )
}

fun CreateActivityCommand.toCreateActivityQuery(): CreateActivityQuery {
    return CreateActivityQuery(
        title = this.title,
        contents = this.contents,
        startAt = this.startAt,
        venue = this.venue,
        venueUrl = this.venueUrl,
        participantsLimit = this.participantsLimit,
        participationFee = this.participationFee,
        course = this.course,
        courseDetail = this.courseDetail,
        path = this.path,
        myUserId = this.myUserId
    )
}

fun ModifyActivityCommand.toModifyActivityQuery(): ModifyActivityQuery {
    return ModifyActivityQuery(
        activityId = this.activityId,
        title = this.title,
        contents = this.contents,
        startAt = this.startAt,
        venue = this.venue,
        venueUrl = this.venueUrl,
        participantsLimit = this.participantsLimit,
        participationFee = this.participationFee,
        course = this.course,
        courseDetail = this.courseDetail,
        path = this.path
    )
}

fun SearchAllActivityCommand.toSearchAllActivityQuery(): SearchAllActivityQuery {
    return SearchAllActivityQuery(
        courses = this.courses,
        recruitmentType = this.recruitmentType,
        myCrewId = this.myCrewId,
        myUserId = this.myUserId
    )
}

fun List<ActivityEntity>.toActivityResults(userId: Long): List<ActivityResult> {
    return this.map { it.toActivityResult(userId) }
}

fun ActivityEntity.toActivityResult(userId: Long): ActivityResult {
    return ActivityResult(
        id = this.id,
        title = this.title,
        contents = this.contents,
        startAt = this.startAt,
        venue = this.venue,
        venueUrl = this.venueUrl,
        participantsLimit = this.participantsLimit,
        participantsQty = this.activityParticipationEntities.size,
        participationFee = this.participationFee,
        course = this.course,
        courseDetail = this.courseDetail,
        path = this.path,
        isOpen = this.isOpen,
        updatedAt = this.updatedAt,
        registeredAt = this.registeredAt,
        recruitmentType = convertRecruitmentType(userId),
        host = this.toHost()
    )
}

fun ActivityEntity.toActivityResult(accessCode: Int): ActivityResult {
    return ActivityResult(
        id = this.id,
        title = this.title,
        contents = this.contents,
        startAt = this.startAt,
        venue = this.venue,
        venueUrl = this.venueUrl,
        participantsLimit = this.participantsLimit,
        participantsQty = this.activityParticipationEntities.size,
        participationFee = this.participationFee,
        course = this.course,
        courseDetail = this.courseDetail,
        path = this.path,
        attendanceCode = accessCode,
        isOpen = this.isOpen,
        updatedAt = this.updatedAt,
        registeredAt = this.registeredAt,
        host = this.toHost()
    )
}

fun AttendanceActivityCommand.toAttendanceActivityQuery(): AttendanceActivityQuery {
    return AttendanceActivityQuery(
        activityId = this.activityId,
        accessCode = this.accessCode,
        myUserId = this.myUserId
    )
}

fun ParticipateActivityCommand.toParticipateActivityQuery(): ParticipateActivityQuery {
    return ParticipateActivityQuery(
        activityId = this.activityId,
        myUserId = this.myUserId
    )
}

fun List<ActivityParticipationEntity>.toParticipantResult(): ParticipantResult{
    val host = this.first().activityEntity!!.userEntity!!
    return ParticipantResult(
        host = host.toParticipant(),
        participants = this.mapToParticipants()
    )
}

fun ParticipateActivityQuery.toActivityParticipationEntity(): ActivityParticipationEntity {
    return ActivityParticipationEntity(
        activityId = this.activityId,
        userId = this.myUserId
    )
}

fun CreateActivityQuery.toActivityEntity(): ActivityEntity {
    return ActivityEntity(
        title = this.title,
        contents = this.contents,
        startAt = this.startAt,
        venue = this.venue,
        venueUrl = this.venueUrl,
        participantsLimit = this.participantsLimit,
        participationFee = this.participationFee,
        course = this.course,
        courseDetail = this.courseDetail,
        path = this.path,
        userId = this.myUserId
    )
}

fun List<ActivityParticipationEntity>.mapToParticipants(): List<ParticipantResult.Participant> {
    return this.map { entity ->
        val userEntity = entity.userEntity!!

        ParticipantResult.Participant(
            participationId = entity.id,
            userId = userEntity.id,
            userName = userEntity.name!!,
            crewName = userEntity.crewEntity!!.name,
            userProfileUri = userEntity.getProfileImageUri()
        )
    }
}

fun UserEntity.toParticipant(): ParticipantResult.Participant {
    return ParticipantResult.Participant(
        userId = this.id,
        userName = this.name!!,
        crewName = this.crewEntity!!.name,
        userProfileUri = this.getProfileImageUri()
    )
}

fun ActivityEntity.toParticipantResult(): ParticipantResult {
    return ParticipantResult(
        host = this.userEntity!!.toParticipant()
    )
}

fun ActivityEntity.toHost(): ActivityResult.Host {
    val host = this.userEntity!!
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

fun ActivityEntity.convertRecruitmentType(myUserId: Long): ActivityRecruitmentType {
    if (this.activityParticipationEntities.any { it.userEntity?.id == myUserId }) {
        return ActivityRecruitmentType.ALREADY_PARTICIPATING
    }
    if (this.startAt.isBefore(LocalDateTime.now())) {
        return ActivityRecruitmentType.ENDED
    }
    if (this.participantsLimit >= this.activityParticipationEntities.size) {
        return ActivityRecruitmentType.CLOSED
    }
    return ActivityRecruitmentType.RECRUITING
}
