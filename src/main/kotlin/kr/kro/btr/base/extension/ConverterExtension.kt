package kr.kro.btr.base.extension

import kr.kro.btr.adapter.`in`.web.payload.*
import kr.kro.btr.adapter.out.thirdparty.model.*
import kr.kro.btr.core.event.model.*
import kr.kro.btr.domain.constant.*
import kr.kro.btr.domain.entity.*
import kr.kro.btr.domain.port.model.*
import kr.kro.btr.domain.port.model.result.ActivityResult
import kr.kro.btr.domain.port.model.result.AnnounceResult
import kr.kro.btr.domain.port.model.result.BornToRunUser
import kr.kro.btr.domain.port.model.result.CommentDetailResult
import kr.kro.btr.domain.port.model.result.CommentResult
import kr.kro.btr.domain.port.model.result.CrewMemberResult
import kr.kro.btr.domain.port.model.result.CrewResult
import kr.kro.btr.domain.port.model.result.FeedDetailResult
import kr.kro.btr.domain.port.model.result.FeedResult
import kr.kro.btr.domain.port.model.result.MarathonDetailResult
import kr.kro.btr.domain.port.model.result.MarathonResult
import kr.kro.btr.domain.port.model.result.ObjectStorageResult
import kr.kro.btr.domain.port.model.result.ParticipantResult
import kr.kro.btr.domain.port.model.result.UserPrivacyResult
import kr.kro.btr.infrastructure.model.*
import kr.kro.btr.support.TokenDetail
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime

// activity
fun List<ActivityResult>.toSearchActivityResponse(): SearchActivitiesResponse {
    val activities = this.map { activityResult ->
        SearchActivitiesResponse.Activity(
            id = activityResult.id,
            title = activityResult.title,
            host = SearchActivitiesResponse.Host(
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
            recruitmentType = activityResult.recruitmentType,
            imageUrls = activityResult.imageUrls
        )
    }

    return SearchActivitiesResponse(activities)
}

fun ActivityResult.toSearchActivityDetailResponse(): DetailActivityResponse {
    return DetailActivityResponse(
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
        host = DetailActivityResponse.Host(
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
        imageIds = this.imageIds,
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

fun SearchActivitiesRequest.toSearchAllActivityCommand(my: TokenDetail): SearchAllActivityCommand {
    return SearchAllActivityCommand(
        courses = this.courses,
        recruitmentType = this.recruitmentType,
        myCrewId = my.crewId,
        myUserId = my.id
    )
}

fun SearchActivitiesRequest.toSearchByCrewIdActivityCommand(crewId: Long): SearchByCrewIdActivityCommand {
    return SearchByCrewIdActivityCommand(
        crewId = crewId,
        courses = this.courses,
        recruitmentType = this.recruitmentType
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
        imageIds = this.imageIds,
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

fun SearchByCrewIdActivityCommand.toSearchByCrewIdActivityQuery(): SearchAllActivityQuery {
    return SearchAllActivityQuery(
        courses = this.courses,
        recruitmentType = this.recruitmentType,
        myCrewId = this.crewId,
        myUserId = 0L
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
        imageUrls = this.getImageUris(),
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
        imageUrls = this.getImageUris(),
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

fun List<ActivityParticipationEntity>.toParticipantResult(): ParticipantResult {
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

// comment
fun List<CommentResult>.toSearchCommentResponse(): SearchCommentsResponse {
    val comments = this.map { it.toSearchCommentResponseComment() }
    return SearchCommentsResponse(comments)
}

fun CommentResult.toSearchCommentResponseComment(): SearchCommentsResponse.Comment {
    return SearchCommentsResponse.Comment(
        id = this.id,
        parentId = this.parentId,
        reCommentQty = this.reCommentQty,
        writer = this.writer.toSearchCommentResponseWriter(),
        contents = this.contents,
        registeredAt = this.registeredAt,
        isMyComment = this.isMyComment
    )
}

fun CommentDetailResult.toSearchCommentDetailResponse(): DetailCommentResponse {
    return DetailCommentResponse(
        id = this.id,
        writer = this.writer.toSearchCommentDetailResponseWriter(),
        contents = this.contents,
        registeredAt = this.registeredAt,
        reComments = this.reCommentResults.toReComments()
    )
}

fun CommentResult.toModifyCommentResponse(): ModifyCommentResponse {
    return ModifyCommentResponse(
        id = this.id,
        contents = this.contents
    )
}

fun CommentResult.Writer.toSearchCommentResponseWriter(): SearchCommentsResponse.Writer {
    return SearchCommentsResponse.Writer(
        userId = this.userId,
        userName = this.userName,
        profileImageUri = this.profileImageUri,
        crewName = this.crewName,
        isAdmin = this.isAdmin,
        isManager = this.isManager
    )
}

fun CommentDetailResult.Writer.toSearchCommentDetailResponseWriter(): DetailCommentResponse.Writer {
    return DetailCommentResponse.Writer(
        userId = this.userId,
        userName = this.userName,
        profileImageUri = this.profileImageUri,
        crewName = this.crewName,
        isAdmin = this.isAdmin,
        isManager = this.isManager
    )
}

fun CommentResult.Writer.toReCommentWriter(): DetailCommentResponse.ReComment.Writer {
    return DetailCommentResponse.ReComment.Writer(
        userId = this.userId,
        userName = this.userName,
        profileImageUri = this.profileImageUri,
        crewName = this.crewName,
        isAdmin = this.isAdmin,
        isManager = this.isManager
    )
}

fun List<CommentResult>.toReComments(): List<DetailCommentResponse.ReComment> {
    return this.map { it.toReComment() }
}

fun CommentResult.toReComment(): DetailCommentResponse.ReComment {
    return DetailCommentResponse.ReComment(
        id = this.id,
        contents = this.contents,
        registeredAt = this.registeredAt,
        writer = this.writer.toReCommentWriter(),
        isMyComment = this.isMyComment
    )
}

fun CreateCommentRequest.toCreateCommentCommand(userId: Long, feedId: Long): CreateCommentCommand {
    return CreateCommentCommand(
        myUserId = userId,
        feedId = feedId,
        parentCommentId = this.parentCommentId,
        contents = this.contents
    )
}

fun ModifyCommentRequest.toModifyCommentCommand(commentId: Long): ModifyCommentCommand {
    return ModifyCommentCommand(
        commentId = commentId,
        contents = this.contents
    )
}

fun CommentEntity.toCommentResult(userId: Long): CommentResult {
    val writer = this.userEntity!!
    return CommentResult(
        id = this.id,
        parentId = this.parentId,
        reCommentQty = this.child.size,
        feedId = this.feedId,
        contents = this.contents,
        registeredAt = this.registeredAt,
        updatedAt = this.updatedAt,
        isMyComment = this.userId == userId,
        writer = writer.toCommentResultWriter()
    )
}

fun CommentEntity.toCommentResult(): CommentResult {
    val writer = this.userEntity!!
    return CommentResult(
        id = this.id,
        parentId = this.parentId,
        feedId = this.feedId,
        contents = this.contents,
        registeredAt = this.registeredAt,
        updatedAt = this.updatedAt,
        writer = writer.toCommentResultWriter()
    )
}

fun List<CommentEntity>.toCommentResults(userId: Long): List<CommentResult> {
    return this.map { it.toCommentResult(userId) }
}

fun CommentEntity.toCommentDetail(commentResults: List<CommentResult>): CommentDetailResult {
    val writer = this.userEntity!!
    return CommentDetailResult(
        id = this.id,
        parentId = this.parentId,
        feedId = this.feedId,
        contents = this.contents,
        registeredAt = this.registeredAt,
        updatedAt = this.updatedAt,
        reCommentResults = commentResults,
        writer = writer.toCommentDetailWriter()
    )
}

fun CreateCommentCommand.toCreateCommentQuery(): CreateCommentQuery {
    return CreateCommentQuery(
        myUserId = this.myUserId,
        feedId = this.feedId,
        parentCommentId = this.parentCommentId,
        contents = this.contents
    )
}

fun ModifyCommentCommand.toModifyCommentQuery(): ModifyCommentQuery {
    return ModifyCommentQuery(
        commentId = this.commentId,
        contents = this.contents
    )
}

fun CreateCommentQuery.toCommentEntity(): CommentEntity {
    return CommentEntity(
        userId = this.myUserId,
        feedId = this.feedId,
        parentId = this.parentCommentId,
        contents = this.contents
    )
}

fun UserEntity.toCommentResultWriter(): CommentResult.Writer {
    return CommentResult.Writer(
        userId = this.id,
        userName = this.name!!,
        profileImageUri = this.getProfileImageUri(),
        crewName = this.crewEntity!!.name,
        isAdmin = this.getIsAdmin(),
        isManager = this.getIsManager()
    )
}

fun UserEntity.toCommentDetailWriter(): CommentDetailResult.Writer {
    return CommentDetailResult.Writer(
        userId = this.id,
        userName = this.name!!,
        profileImageUri = this.getProfileImageUri(),
        crewName = this.crewEntity!!.name,
        isAdmin = this.getIsAdmin(),
        isManager = this.getIsManager()
    )
}

// crew
fun List<CrewResult>.toSearchCrewResponse(): SearchCrewsResponse {
    val crewDetails = this.map { it.toCrewDetail() }
    return SearchCrewsResponse(crewDetails)
}

fun CrewResult.toCrewDetail(): SearchCrewsResponse.Detail {
    return SearchCrewsResponse.Detail(
        id = this.id,
        crewName = this.name,
        contents = this.contents,
        region = this.region,
        imageUri = this.imageUri,
        logoUri = this.logoUri,
        crewSnsUri = this.sns
    )
}

fun CrewResult.toDetailCrewResponse(): DetailCrewResponse {
    return DetailCrewResponse(
        id = this.id,
        crewName = this.name,
        contents = this.contents,
        region = this.region,
        imageUri = this.imageUri,
        logoUri = this.logoUri,
        crewSnsUri = this.sns
    )
}

fun CreateCrewRequest.toCreateCrewCommand(): CreateCrewCommand {
    return CreateCrewCommand(
        name = this.name,
        contents = this.contents,
        sns = this.sns,
        region = this.region
    )
}

fun CrewEntity.toCrew(): CrewResult {
    return CrewResult(
        id = this.id,
        name = this.name,
        contents = this.contents,
        region = this.region,
        sns = this.sns,
        imageUri = this.imageEntity?.fileUri,
        logoUri = this.logoEntity?.fileUri
    )
}

fun List<CrewEntity>.toCrews(): List<CrewResult> {
    return this.map { it.toCrew() }
}

fun CreateCrewCommand.toCreateCrewQuery(): CreateCrewQuery {
    return CreateCrewQuery(
        name = this.name,
        contents = this.contents,
        sns = this.sns,
        region = this.region
    )
}

fun CreateCrewQuery.toCrewEntity(): CrewEntity {
    return CrewEntity(
        name = this.name,
        contents = this.contents,
        sns = this.sns,
        region = this.region
    )
}

// crew members
fun List<CrewMemberResult>.toSearchCrewMembersResponse(): SearchCrewMembersResponse {
    val members = this.map { it.toCrewMember() }
    return SearchCrewMembersResponse(members)
}

fun CrewMemberResult.toCrewMember(): SearchCrewMembersResponse.Member {
    return SearchCrewMembersResponse.Member(
        userId = this.userId,
        userName = this.userName,
        profileImageUri = this.profileImageUri,
        instagramId = this.instagramId,
        isManager = this.isManager,
        isAdmin = this.isAdmin
    )
}

fun List<UserEntity>.toCrewMembers(): List<CrewMemberResult> {
    return this.map { it.toCrewMember() }
}

fun UserEntity.toCrewMember(): CrewMemberResult {
    return CrewMemberResult(
        userId = this.id,
        userName = this.name ?: "Unknown",
        profileImageUri = this.getProfileImageUri(),
        instagramId = this.instagramId,
        isManager = this.getIsManager(),
        isAdmin = this.getIsAdmin()
    )
}

// feed
fun FeedDetailResult.toDetailFeedResponse(): DetailFeedResponse {
    return DetailFeedResponse(
        id = this.id,
        contents = this.contents,
        images = this.images.toDetailFeedResponseImages(),
        category = this.category,
        accessLevel = this.accessLevel,
        viewQty = this.viewQty,
        recommendationQty = this.recommendationQty,
        commentQty = this.commentQty,
        registeredAt = this.registeredAt,
        writer = this.writer.toDetailFeedResponseWriter(),
        viewer = DetailFeedResponse.Viewer(
            hasMyRecommendation = this.hasMyRecommendation,
            hasMyComment = this.hasMyComment
        )
    )
}

fun FeedDetailResult.Writer.toDetailFeedResponseWriter(): DetailFeedResponse.Writer {
    return DetailFeedResponse.Writer(
        userId = this.userId,
        userName = this.userName,
        crewName = this.crewName,
        profileImageUri = this.profileImageUri,
        isAdmin = this.isAdmin,
        isManager = this.isManager
    )
}

fun List<FeedDetailResult.Image>?.toDetailFeedResponseImages(): List<DetailFeedResponse.Image>? {
    return this?.map { it.toDetailFeedResponseImage() }
}

fun FeedDetailResult.Image.toDetailFeedResponseImage(): DetailFeedResponse.Image {
    return DetailFeedResponse.Image(
        imageId = this.id,
        imageUri = this.imageUri
    )
}

fun FeedResult.toSearchFeedResponse(): SearchFeedsResponse {
    return SearchFeedsResponse(
        id = this.id,
        imageUris = this.imageUris,
        contents = this.contents,
        viewQty = this.viewQty,
        recommendationQty = this.recommendationQty,
        commentQty = this.commentQty,
        registeredAt = this.registeredAt,
        writer = this.writer.toSearchFeedResponseWriter(),
        viewer = SearchFeedsResponse.Viewer(
            hasMyRecommendation = this.hasRecommendation,
            hasMyComment = this.hasComment
        )
    )
}

fun FeedResult.Writer.toSearchFeedResponseWriter(): SearchFeedsResponse.Writer {
    return SearchFeedsResponse.Writer(
        userName = this.userName,
        crewName = this.crewName,
        profileImageUri = this.profileImageUri,
        isAdmin = this.isAdmin,
        isManager = this.isManager
    )
}

fun TokenDetail.toSearchFeedDetailCommand(feedId: Long): SearchFeedDetailCommand {
    return SearchFeedDetailCommand(
        my = this,
        feedId = feedId
    )
}

fun SearchFeedRequest.toSearchAllFeedCommand(my: TokenDetail, feedId: Long): SearchAllFeedCommand {
    return SearchAllFeedCommand(
        category = this.category,
        searchKeyword = this.searchKeyword,
        isMyCrew = this.isMyCrew,
        my = my,
        lastFeedId = feedId
    )
}

fun CreateFeedRequest.toCreateFeedCommand(my: TokenDetail): CreateFeedCommand {
    return CreateFeedCommand(
        imageIds = this.imageIds,
        contents = this.contents,
        category = this.category,
        accessLevel = this.accessLevel,
        myUserId = my.id
    )
}

fun TokenDetail.toRemoveFeedCommand(feedId: Long): RemoveFeedCommand {
    return RemoveFeedCommand(
        feedId = feedId,
        my = this
    )
}

fun ModifyFeedRequest.toModifyFeedCommand(feedId: Long): ModifyFeedCommand {
    return ModifyFeedCommand(
        imageIds = this.imageIds,
        contents = this.contents,
        category = this.category,
        accessLevel = this.accessLevel,
        feedId = feedId
    )
}

fun CreateFeedQuery.toFeedEntity(): FeedEntity {
    return FeedEntity(
        userId = this.userId,
        contents = this.contents,
        category = this.category,
        accessLevel = this.accessLevel
    )
}

fun FeedEntity.toFeedResult(my: TokenDetail): FeedDetailResult {
    val writer = this.userEntity!!
    val images = this.feedImageMappingEntities.mapNotNull { image ->
        image.objectStorageEntity?.let {
            FeedDetailResult.Image(
                id = it.id,
                imageUri = it.fileUri
            )
        }
    }

    return FeedDetailResult(
        id = this.id,
        contents = this.contents,
        category = this.category,
        accessLevel = this.accessLevel,
        viewQty = this.viewQty,
        registeredAt = this.registeredAt,
        updatedAt = this.updatedAt,
        images = images.ifEmpty { null },
        writer = writer.toFeedResultWriter(),
        recommendationQty = this.recommendationEntities.size,
        hasMyRecommendation = this.hasMyRecommendation(my.id),
        commentQty = this.commentEntities.size,
        hasMyComment = this.hasMyComment(my.id)
    )
}

fun UserEntity.toFeedResultWriter(): FeedDetailResult.Writer {
    return FeedDetailResult.Writer(
        userId = this.id,
        userName = this.name!!,
        crewName = this.crewEntity!!.name,
        profileImageUri = this.getProfileImageUri(),
        isAdmin = this.getIsAdmin(),
        isManager = this.getIsManager()
    )
}

fun SearchAllFeedCommand.toSearchAllFeedQuery(userIds: List<Long>): SearchAllFeedQuery {
    return SearchAllFeedQuery(
        category = this.category,
        searchKeyword = this.searchKeyword,
        isMyCrew = this.isMyCrew,
        my = this.my,
        lastFeedId = this.lastFeedId,
        searchedUserIds = userIds
    )
}

fun FeedEntity.toFeedCard(userId: Long): FeedResult {
    val writer = this.userEntity!!
    return FeedResult(
        id = this.id,
        imageUris = this.getImageUris(),
        contents = this.contents,
        viewQty = this.viewQty,
        recommendationQty = this.getRecommendationQty(),
        commentQty = this.getCommentQty(),
        registeredAt = this.registeredAt,
        hasRecommendation = this.hasMyRecommendation(userId),
        hasComment = this.hasMyComment(userId),
        writer = writer.toFeedCardWriter()
    )
}

fun UserEntity.toFeedCardWriter(): FeedResult.Writer {
    return FeedResult.Writer(
        userName = this.name!!,
        crewName = this.crewEntity!!.name,
        profileImageUri = this.getProfileImageUri(),
        isAdmin = this.getIsAdmin(),
        isManager = this.getIsManager()
    )
}

fun CreateFeedCommand.toCreateFeedQuery(): CreateFeedQuery {
    return CreateFeedQuery(
        contents = this.contents,
        imageIds = this.imageIds,
        category = this.category,
        accessLevel = this.accessLevel,
        userId = this.myUserId
    )
}

fun ModifyFeedCommand.toModifyFeedQuery(): ModifyFeedQuery {
    return ModifyFeedQuery(
        feedId = this.feedId,
        imageIds = this.imageIds,
        contents = this.contents,
        category = this.category,
        accessLevel = this.accessLevel
    )
}

// marathon bookmark
fun BookmarkMarathonCommand.toBookmarkMarathonQuery(): BookmarkMarathonQuery {
    return BookmarkMarathonQuery(
        marathonId = this.marathonId,
        myUserId = this.myUserId
    )
}

fun CancelBookmarkMarathonCommand.toBookmarkMarathonQuery(): BookmarkMarathonQuery {
    return BookmarkMarathonQuery(
        marathonId = this.marathonId,
        myUserId = this.myUserId
    )
}

fun BookmarkMarathonQuery.toMarathonBookmarkEntity(): MarathonBookmarkEntity {
    return MarathonBookmarkEntity(
        userId = this.myUserId,
        marathonId = this.marathonId
    )
}

// marathon
fun List<MarathonResult>.toSearchAllMarathonResponse(): SearchMarathonsResponse {
    val marathons = this.map { it.toSearchAllMarathonResponseMarathon() }
    return SearchMarathonsResponse(details = marathons)
}

fun MarathonResult.toSearchAllMarathonResponseMarathon(): SearchMarathonsResponse.Marathon {
    return SearchMarathonsResponse.Marathon(
        id = this.id,
        title = this.title,
        schedule = this.schedule,
        venue = this.venue,
        course = this.course,
        isBookmarking = this.isBookmarking == true
    )
}

fun MarathonDetailResult.toSearchMarathonDetailResponse(): DetailMarathonResponse {
    return DetailMarathonResponse(
        id = this.id,
        title = this.title,
        owner = this.owner,
        email = this.email,
        schedule = this.schedule,
        contact = this.contact,
        course = this.course,
        location = this.location,
        venue = this.venue,
        host = this.host,
        duration = this.duration,
        homepage = this.homepage,
        venueDetail = this.venueDetail,
        remark = this.remark,
        registeredAt = this.registeredAt,
        isBookmarking = this.isBookmarking
    )
}

fun SearchMarathonsRequest.toSearchAllMarathonCommand(userId: Long): SearchAllMarathonCommand {
    return SearchAllMarathonCommand(
        locations = this.locations,
        courses = this.courses,
        myUserId = userId
    )
}

fun TokenDetail.toSearchMarathonDetailCommand(marathonId: Long): SearchMarathonDetailCommand {
    return SearchMarathonDetailCommand(
        marathonId = marathonId,
        myUserId = this.id
    )
}

fun TokenDetail.toBookmarkMarathonCommand(marathonId: Long): BookmarkMarathonCommand {
    return BookmarkMarathonCommand(
        marathonId = marathonId,
        myUserId = this.id
    )
}

fun TokenDetail.toCancelBookmarkMarathonCommand(marathonId: Long): CancelBookmarkMarathonCommand {
    return CancelBookmarkMarathonCommand(
        marathonId = marathonId,
        myUserId = this.id
    )
}

fun SearchAllMarathonCommand.toSearchMarathonQuery(): SearchMarathonQuery {
    return SearchMarathonQuery(
        locations = this.locations,
        courses = this.courses
    )
}

fun List<MarathonEntity>.toMarathons(userId: Long): List<MarathonResult> {
    return this.map { it.toMarathon(userId) }
}

fun MarathonEntity.toMarathon(userId: Long): MarathonResult {
    return MarathonResult(
        id = this.id,
        title = this.title,
        schedule = this.schedule,
        venue = this.venue,
        course = this.course,
        isBookmarking = this.marathonBookmarkEntities.any { userId == it.userId }
    )
}

fun MarathonEntity.toMarathonDetail(userId: Long): MarathonDetailResult {
    return MarathonDetailResult(
        id = this.id,
        title = this.title,
        owner = this.owner,
        email = this.email,
        schedule = this.schedule,
        contact = this.contact,
        course = this.course,
        location = this.location,
        venue = this.venue,
        host = this.host,
        duration = this.duration,
        homepage = this.homepage,
        venueDetail = this.venueDetail,
        remark = this.remark,
        registeredAt = this.registeredAt,
        isBookmarking = this.marathonBookmarkEntities.any { userId == it.userId }
    )
}

// object storage
fun ObjectStorageResult.toUploadFileResponse(): UploadFileResponse {
    return UploadFileResponse(
        fileId = this.id,
        fileUri = this.fileUri
    )
}

fun TokenDetail.toUploadObjectStorageCommand(file: MultipartFile, bucket: Bucket): UploadObjectStorageCommand {
    return UploadObjectStorageCommand(
        myUserId = this.id,
        file = file,
        bucket = bucket
    )
}

fun TokenDetail.toRemoveObjectStorageCommand(fileId: Long, bucket: Bucket): RemoveObjectStorageCommand {
    return RemoveObjectStorageCommand(
        my = this,
        targetFileId = fileId,
        bucket = bucket
    )
}

fun UploadObjectStorageCommand.toUploadObjectStorageQuery(): UploadObjectStorageQuery {
    return UploadObjectStorageQuery(
        myUserId = this.myUserId,
        file = this.file,
        bucket = this.bucket
    )
}

fun ObjectStorageEntity.toObjectStorage(): ObjectStorageResult {
    return ObjectStorageResult(
        id = this.id,
        userId = this.userId,
        fileUri = this.fileUri,
        uploadAt = this.uploadAt
    )
}

fun RemoveObjectStorageCommand.toRemoveObjectStorageQuery(): RemoveObjectStorageQuery {
    return RemoveObjectStorageQuery(
        my = this.my,
        targetFileId = this.targetFileId,
        bucket = this.bucket
    )
}

fun UploadObjectStorageQuery.toUpload(): Upload {
    return Upload(
        file = this.file,
        myUserId = this.myUserId,
        bucket = this.bucket
    )
}

fun ModifyObjectStorageQuery.toUpload(): Upload {
    return Upload(
        file = this.file,
        myUserId = this.my.id,
        bucket = this.bucket
    )
}

fun MinioRemoveEventModel.toRemove(): Remove {
    return Remove(
        bucket = this.bucket,
        objectName = this.objectName
    )
}

fun MinioRemoveAllEventModel.toRemoveAll(): RemoveAll {
    return RemoveAll(
        bucket = this.bucket,
        objectNames = this.objectNames
    )
}

// privacy
fun UserPrivacyResult.toSearchUserPrivacyResponse(): DetailUserPrivacyResponse {
    return DetailUserPrivacyResponse(
        isInstagramIdPublic = this.isInstagramIdPublic
    )
}

fun SettingUserPrivacyRequest.toModifyUserPrivacyCommand(userId: Long): ModifyUserPrivacyCommand {
    return ModifyUserPrivacyCommand(
        myUserId = userId,
        isInstagramIdPublic = this.isInstagramIdPublic
    )
}

fun ModifyUserPrivacyCommand.toModifyUserPrivacyQuery(): ModifyUserPrivacyQuery {
    return ModifyUserPrivacyQuery(
        myUserId = this.myUserId,
        isInstagramIdPublic = this.isInstagramIdPublic
    )
}

fun UserPrivacyEntity.toUserPrivacy(): UserPrivacyResult {
    return UserPrivacyResult(
        id = this.id,
        userId = this.userId,
        isInstagramIdPublic = this.isInstagramIdPublic
    )
}

// recommandation
fun TokenDetail.toCreateRecommendationCommand(recommendationType: RecommendationType, contentId: Long): CreateRecommendationCommand {
    return CreateRecommendationCommand(
        recommendationType = recommendationType,
        contentId = contentId,
        myUserId = this.id
    )
}

fun TokenDetail.toRemoveRecommendationCommand(recommendationType: RecommendationType, contentId: Long): RemoveRecommendationCommand {
    return RemoveRecommendationCommand(
        recommendationType = recommendationType,
        contentId = contentId,
        myUserId = this.id
    )
}

fun CreateRecommendationCommand.toCreateRecommendationQuery(): CreateRecommendationQuery {
    return CreateRecommendationQuery(
        recommendationType = this.recommendationType,
        contentId = this.contentId,
        myUserId = this.myUserId
    )
}

fun RemoveRecommendationCommand.toRemoveRecommendationQuery(): RemoveRecommendationQuery {
    return RemoveRecommendationQuery(
        recommendationType = this.recommendationType,
        contentId = this.contentId,
        myUserId = this.myUserId
    )
}

fun CreateRecommendationQuery.toRecommendationEntity(): RecommendationEntity {
    return RecommendationEntity(
        userId = this.myUserId,
        contentId = this.contentId,
        recommendationType = this.recommendationType
    )
}

// user
fun BornToRunUser.toUserDetailResponse(): DetailUserResponse {
    return DetailUserResponse(
        userId = this.userId,
        userName = this.userName,
        crewName = this.crewName,
        profileImageUri = this.profileImageUri,
        isAdmin = this.isAdmin,
        isManager = this.isManager,
        yellowCardQty = this.yellowCardQty,
        instagramId = this.instagramId,
        isInstagramIdPublic = this.isInstagramIdPublic
    )
}

fun BornToRunUser.toModifyUserResponse(): ModifyUserResponse {
    return ModifyUserResponse(
        userName = this.userName,
        crewName = this.crewName,
        instagramId = this.instagramId,
        profileImageUri = this.profileImageUri
    )
}

fun SignUpRequest.toSignUpCommand(userId: Long): SignUpCommand {
    return SignUpCommand(
        userId = userId,
        userName = this.userName,
        crewId = this.crewId,
        instagramId = this.instagramId
    )
}

fun ModifyUserRequest.toModifyUserCommand(userId: Long): ModifyUserCommand {
    return ModifyUserCommand(
        userId = userId,
        profileImageId = this.profileImageId,
        instagramId = this.instagramId
    )
}

fun SignUpCommand.toSignUpUserQuery(): SignUpUserQuery {
    return SignUpUserQuery(
        userId = this.userId,
        userName = this.userName,
        crewId = this.crewId,
        instagramId = this.instagramId,
        roleType = RoleType.MEMBER
    )
}

fun ModifyUserCommand.toModifyUserQuery(): ModifyUserQuery {
    return ModifyUserQuery(
        userId = this.userId,
        profileImageId = this.profileImageId,
        instagramId = this.instagramId
    )
}

fun CreateUserCommand.toCreateUserQuery(): CreateUserQuery {
    return CreateUserQuery(
        socialId = this.socialId,
        providerType = this.providerType,
        roleType = this.roleType
    )
}

fun CreateUserQuery.toUserEntity(): UserEntity {
    return UserEntity(
        socialId = this.socialId,
        providerType = this.providerType,
        roleType = this.roleType
    )
}

fun UserEntity.toBornToRunUser(): BornToRunUser {
    return BornToRunUser(
        userId = this.id,
        socialId = this.socialId,
        providerType = this.providerType,
        roleType = this.roleType,
        userName = this.name,
        crewId = this.crewId,
        crewName = this.crewEntity?.name,
        instagramId = this.instagramId,
        imageId = this.imageId,
        profileImageUri = this.getProfileImageUri(),
        lastLoginAt = this.lastLoginAt,
        isAdmin = this.getIsAdmin(),
        isManager = this.getIsManager(),
        yellowCardQty = this.yellowCardQty,
        isInstagramIdPublic = this.userPrivacyEntity?.isInstagramIdPublic
    )
}

// user refresh token
fun CreateRefreshTokenCommand.toCreateRefreshTokenQuery(): CreateRefreshTokenQuery {
    return CreateRefreshTokenQuery(
        userId = this.userId,
        refreshToken = this.refreshToken
    )
}

// yellowcard
fun CreateYellowCardRequest.toCreateYellowCardCommand(userId: Long): CreateYellowCardCommand {
    return CreateYellowCardCommand(
        targetUserId = this.targetUserId,
        sourceUserId = userId,
        reason = this.reason,
        basis = this.basis
    )
}

fun CreateYellowCardCommand.toCreateYellowCardQuery(): CreateYellowCardQuery {
    return CreateYellowCardQuery(
        targetUserId = this.targetUserId,
        sourceUserId = this.sourceUserId,
        reason = this.reason,
        basis = this.basis
    )
}

fun CreateYellowCardQuery.toYellowCardEntity(): YellowCardEntity {
    return YellowCardEntity(
        targetUserId = this.targetUserId,
        sourceUserId = this.sourceUserId,
        reason = this.reason,
        basis = this.basis
    )
}

// announce
fun CreateAnnounceRequest.toCreateAnnounceCommand(userId: Long): CreateAnnounceCommand {
    return CreateAnnounceCommand(
        title = this.title,
        contents = this.contents,
        postedAt = this.postedAt,
        userId = userId
    )
}

fun CreateAnnounceCommand.toCreateAnnounceQuery(): CreateAnnounceQuery {
    return CreateAnnounceQuery(
        title = this.title,
        contents = this.contents,
        postedAt = this.postedAt,
        myUserId = this.userId
    )
}

fun CreateAnnounceQuery.toAnnounceEntity(): AnnounceEntity {
    return AnnounceEntity(
        title = this.title,
        contents = this.contents,
        postedAt = this.postedAt,
        userId = this.myUserId
    )
}

fun ModifyAnnounceRequest.toModifyAnnounceCommand(announceId: Long): ModifyAnnounceCommand {
    return ModifyAnnounceCommand(
        id = announceId,
        title = this.title,
        contents = this.contents,
        postedAt = this.postedAt
    )
}

fun ModifyAnnounceCommand.toModifyAnnounceQuery(): ModifyAnnounceQuery {
    return ModifyAnnounceQuery(
        id = this.id,
        title = this.title,
        contents = this.contents,
        postedAt = this.postedAt
    )
}

fun AnnounceEntity.toAnnounceResult(): AnnounceResult {
    return AnnounceResult(
        id = this.id,
        title = this.title,
        contents = this.contents,
        writer = AnnounceResult.Writer(
            userId = this.userId,
            name = this.userEntity!!.name!!
        )
    )
}

fun List<AnnounceEntity>.toAnnounceResults(): List<AnnounceResult> {
    return this.map { it.toAnnounceResult() }
}

fun AnnounceResult.toDetailAnnounceResponse(): DetailAnnounceResponse {
    return DetailAnnounceResponse(
        id = this.id,
        title = this.title,
        contents = this.contents,
        writer = DetailAnnounceResponse.Writer(
            userId = this.writer.userId,
            name = this.writer.name
        )
    )
}

fun AnnounceResult.toModifyAnnounceResponse(): ModifyAnnounceResponse {
    return ModifyAnnounceResponse(
        id = this.id,
        title = this.title,
        contents = this.contents
    )
}

fun AnnounceResult.toSearchAnnouncesResponseDetail(): SearchAnnouncesResponse.Detail {
    return SearchAnnouncesResponse.Detail(
        id = this.id,
        title = this.title,
        contents = this.contents,
        writer = SearchAnnouncesResponse.Writer(
            userId = this.writer.userId,
            name = this.writer.name
        )
    )
}

fun List<AnnounceResult>.toSearchAnnouncesResponse(): SearchAnnouncesResponse {
    val details =  this.map { it.toSearchAnnouncesResponseDetail() }
    return SearchAnnouncesResponse(details)
}
