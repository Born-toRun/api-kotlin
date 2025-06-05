package kr.kro.btr.base.extension

import kr.kro.btr.adapter.`in`.web.payload.AttendanceActivityRequest
import kr.kro.btr.adapter.`in`.web.payload.CreateActivityRequest
import kr.kro.btr.adapter.`in`.web.payload.CreateCommentRequest
import kr.kro.btr.adapter.`in`.web.payload.CreateCrewRequest
import kr.kro.btr.adapter.`in`.web.payload.CreateFeedRequest
import kr.kro.btr.adapter.`in`.web.payload.CreateYellowCardRequest
import kr.kro.btr.adapter.`in`.web.payload.DetailCrewResponse
import kr.kro.btr.adapter.`in`.web.payload.DetailFeedResponse
import kr.kro.btr.adapter.`in`.web.payload.ModifyActivityRequest
import kr.kro.btr.adapter.`in`.web.payload.ModifyCommentRequest
import kr.kro.btr.adapter.`in`.web.payload.ModifyCommentResponse
import kr.kro.btr.adapter.`in`.web.payload.ModifyFeedRequest
import kr.kro.btr.adapter.`in`.web.payload.OpenActivityResponse
import kr.kro.btr.adapter.`in`.web.payload.ParticipationActivityResponse
import kr.kro.btr.adapter.`in`.web.payload.SearchActivityDetailResponse
import kr.kro.btr.adapter.`in`.web.payload.SearchActivityResponse
import kr.kro.btr.adapter.`in`.web.payload.SearchAllActivityRequest
import kr.kro.btr.adapter.`in`.web.payload.SearchAllMarathonRequest
import kr.kro.btr.adapter.`in`.web.payload.SearchAllMarathonResponse
import kr.kro.btr.adapter.`in`.web.payload.SearchCommentDetailResponse
import kr.kro.btr.adapter.`in`.web.payload.SearchCommentResponse
import kr.kro.btr.adapter.`in`.web.payload.SearchCrewResponse
import kr.kro.btr.adapter.`in`.web.payload.SearchFeedRequest
import kr.kro.btr.adapter.`in`.web.payload.SearchFeedResponse
import kr.kro.btr.adapter.`in`.web.payload.SearchMarathonDetailResponse
import kr.kro.btr.adapter.`in`.web.payload.SearchUserPrivacyResponse
import kr.kro.btr.adapter.`in`.web.payload.SettingUserPrivacyRequest
import kr.kro.btr.adapter.`in`.web.payload.UploadFileResponse
import kr.kro.btr.adapter.out.thirdparty.model.Remove
import kr.kro.btr.adapter.out.thirdparty.model.RemoveAll
import kr.kro.btr.adapter.out.thirdparty.model.Upload
import kr.kro.btr.core.event.model.MinioRemoveAllEventModel
import kr.kro.btr.core.event.model.MinioRemoveEventModel
import kr.kro.btr.domain.constant.ActivityRecruitmentType
import kr.kro.btr.domain.constant.Bucket
import kr.kro.btr.domain.entity.ActivityEntity
import kr.kro.btr.domain.entity.ActivityParticipationEntity
import kr.kro.btr.domain.entity.CommentEntity
import kr.kro.btr.domain.entity.CrewEntity
import kr.kro.btr.domain.entity.FeedEntity
import kr.kro.btr.domain.entity.MarathonBookmarkEntity
import kr.kro.btr.domain.entity.MarathonEntity
import kr.kro.btr.domain.entity.ObjectStorageEntity
import kr.kro.btr.domain.entity.UserEntity
import kr.kro.btr.domain.entity.UserPrivacyEntity
import kr.kro.btr.domain.entity.YellowCardEntity
import kr.kro.btr.domain.port.model.ActivityResult
import kr.kro.btr.domain.port.model.AttendanceActivityCommand
import kr.kro.btr.domain.port.model.BookmarkMarathonCommand
import kr.kro.btr.domain.port.model.CancelBookmarkMarathonCommand
import kr.kro.btr.domain.port.model.CommentDetail
import kr.kro.btr.domain.port.model.CommentResult
import kr.kro.btr.domain.port.model.CreateActivityCommand
import kr.kro.btr.domain.port.model.CreateCommentCommand
import kr.kro.btr.domain.port.model.CreateCrewCommand
import kr.kro.btr.domain.port.model.CreateFeedCommand
import kr.kro.btr.domain.port.model.CreateRefreshTokenCommand
import kr.kro.btr.domain.port.model.CreateYellowCardCommand
import kr.kro.btr.domain.port.model.Crew
import kr.kro.btr.domain.port.model.FeedCard
import kr.kro.btr.domain.port.model.FeedResult
import kr.kro.btr.domain.port.model.Marathon
import kr.kro.btr.domain.port.model.MarathonDetail
import kr.kro.btr.domain.port.model.ModifyActivityCommand
import kr.kro.btr.domain.port.model.ModifyCommentCommand
import kr.kro.btr.domain.port.model.ModifyFeedCommand
import kr.kro.btr.domain.port.model.ModifyUserPrivacyCommand
import kr.kro.btr.domain.port.model.ObjectStorage
import kr.kro.btr.domain.port.model.ParticipantResult
import kr.kro.btr.domain.port.model.ParticipateActivityCommand
import kr.kro.btr.domain.port.model.RemoveFeedCommand
import kr.kro.btr.domain.port.model.RemoveObjectStorageCommand
import kr.kro.btr.domain.port.model.SearchAllActivityCommand
import kr.kro.btr.domain.port.model.SearchAllFeedCommand
import kr.kro.btr.domain.port.model.SearchAllMarathonCommand
import kr.kro.btr.domain.port.model.SearchFeedDetailCommand
import kr.kro.btr.domain.port.model.SearchMarathonDetailCommand
import kr.kro.btr.domain.port.model.UploadObjectStorageCommand
import kr.kro.btr.domain.port.model.UserPrivacy
import kr.kro.btr.infrastructure.model.AttendanceActivityQuery
import kr.kro.btr.infrastructure.model.BookmarkMarathonQuery
import kr.kro.btr.infrastructure.model.CreateActivityQuery
import kr.kro.btr.infrastructure.model.CreateCommentQuery
import kr.kro.btr.infrastructure.model.CreateCrewQuery
import kr.kro.btr.infrastructure.model.CreateFeedQuery
import kr.kro.btr.infrastructure.model.CreateRefreshTokenQuery
import kr.kro.btr.infrastructure.model.CreateYellowCardQuery
import kr.kro.btr.infrastructure.model.ModifyActivityQuery
import kr.kro.btr.infrastructure.model.ModifyCommentQuery
import kr.kro.btr.infrastructure.model.ModifyFeedQuery
import kr.kro.btr.infrastructure.model.ModifyObjectStorageQuery
import kr.kro.btr.infrastructure.model.ModifyUserPrivacyQuery
import kr.kro.btr.infrastructure.model.ParticipateActivityQuery
import kr.kro.btr.infrastructure.model.RemoveObjectStorageQuery
import kr.kro.btr.infrastructure.model.SearchAllActivityQuery
import kr.kro.btr.infrastructure.model.SearchAllFeedQuery
import kr.kro.btr.infrastructure.model.SearchMarathonQuery
import kr.kro.btr.infrastructure.model.UploadObjectStorageQuery
import kr.kro.btr.support.TokenDetail
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime

// activity
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

// comment
fun List<CommentResult>.toSearchCommentResponse(): SearchCommentResponse {
    val comments = this.map { it.toSearchCommentResponseComment() }
    return SearchCommentResponse(comments)
}

fun CommentResult.toSearchCommentResponseComment(): SearchCommentResponse.Comment {
    return SearchCommentResponse.Comment(
        id = this.id,
        parentId = this.parentId,
        reCommentQty = this.reCommentQty,
        writer = this.writer.toSearchCommentResponseWriter(),
        contents = this.contents,
        registeredAt = this.registeredAt,
        isMyComment = this.isMyComment
    )
}

fun CommentDetail.toSearchCommentDetailResponse(): SearchCommentDetailResponse {
    return SearchCommentDetailResponse(
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

fun CommentResult.Writer.toSearchCommentResponseWriter(): SearchCommentResponse.Writer {
    return SearchCommentResponse.Writer(
        userId = this.userId,
        userName = this.userName,
        profileImageUri = this.profileImageUri,
        crewName = this.crewName,
        isAdmin = this.isAdmin,
        isManager = this.isManager
    )
}

fun CommentDetail.Writer.toSearchCommentDetailResponseWriter(): SearchCommentDetailResponse.Writer {
    return SearchCommentDetailResponse.Writer(
        userId = this.userId,
        userName = this.userName,
        profileImageUri = this.profileImageUri,
        crewName = this.crewName,
        isAdmin = this.isAdmin,
        isManager = this.isManager
    )
}

fun CommentResult.Writer.toReCommentWriter(): SearchCommentDetailResponse.ReComment.Writer {
    return SearchCommentDetailResponse.ReComment.Writer(
        userId = this.userId,
        userName = this.userName,
        profileImageUri = this.profileImageUri,
        crewName = this.crewName,
        isAdmin = this.isAdmin,
        isManager = this.isManager
    )
}

fun List<CommentResult>.toReComments(): List<SearchCommentDetailResponse.ReComment> {
    return this.map { it.toReComment() }
}

fun CommentResult.toReComment(): SearchCommentDetailResponse.ReComment {
    return SearchCommentDetailResponse.ReComment(
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

fun CommentEntity.toCommentDetail(commentResults: List<CommentResult>): CommentDetail {
    val writer = this.userEntity!!
    return CommentDetail(
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

fun UserEntity.toCommentDetailWriter(): CommentDetail.Writer {
    return CommentDetail.Writer(
        userId = this.id,
        userName = this.name!!,
        profileImageUri = this.getProfileImageUri(),
        crewName = this.crewEntity!!.name,
        isAdmin = this.getIsAdmin(),
        isManager = this.getIsManager()
    )
}

// crew
fun List<Crew>.toSearchCrewResponse(): SearchCrewResponse {
    val crewDetails = this.map { it.toCrewDetail() }
    return SearchCrewResponse(crewDetails)
}

fun Crew.toCrewDetail(): SearchCrewResponse.CrewDetail {
    return SearchCrewResponse.CrewDetail(
        id = this.id,
        crewName = this.name,
        contents = this.contents,
        region = this.region,
        imageUri = this.imageUri,
        logoUri = this.logoUri,
        crewSnsUri = this.sns
    )
}

fun Crew.toDetailCrewResponse(): DetailCrewResponse {
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

fun CrewEntity.toCrew(): Crew {
    return Crew(
        id = this.id,
        name = this.name,
        contents = this.contents,
        region = this.region,
        sns = this.sns,
        imageUri = this.imageEntity?.fileUri,
        logoUri = this.logoEntity?.fileUri
    )
}

fun List<CrewEntity>.toCrews(): List<Crew> {
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

// feed
fun FeedResult.toDetailFeedResponse(): DetailFeedResponse {
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

fun FeedResult.Writer.toDetailFeedResponseWriter(): DetailFeedResponse.Writer {
    return DetailFeedResponse.Writer(
        userId = this.userId,
        userName = this.userName,
        crewName = this.crewName,
        profileImageUri = this.profileImageUri,
        isAdmin = this.isAdmin,
        isManager = this.isManager
    )
}

fun List<FeedResult.Image>?.toDetailFeedResponseImages(): List<DetailFeedResponse.Image>? {
    return this?.map { it.toDetailFeedResponseImage() }
}

fun FeedResult.Image.toDetailFeedResponseImage(): DetailFeedResponse.Image {
    return DetailFeedResponse.Image(
        imageId = this.id,
        imageUri = this.imageUri
    )
}

fun FeedCard.toSearchFeedResponse(): SearchFeedResponse {
    return SearchFeedResponse(
        id = this.id,
        imageUris = this.imageUris,
        contents = this.contents,
        viewQty = this.viewQty,
        recommendationQty = this.recommendationQty,
        commentQty = this.commentQty,
        registeredAt = this.registeredAt,
        writer = this.writer.toSearchFeedResponseWriter(),
        viewer = SearchFeedResponse.Viewer(
            hasMyRecommendation = this.hasRecommendation,
            hasMyComment = this.hasComment
        )
    )
}

fun FeedCard.Writer.toSearchFeedResponseWriter(): SearchFeedResponse.Writer {
    return SearchFeedResponse.Writer(
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

fun FeedEntity.toFeedResult(my: TokenDetail): FeedResult {
    val writer = this.userEntity!!
    val images = this.feedImageMappingEntities.mapNotNull { image ->
        image.objectStorageEntity?.let {
            FeedResult.Image(
                id = it.id,
                imageUri = it.fileUri
            )
        }
    }

    return FeedResult(
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

fun UserEntity.toFeedResultWriter(): FeedResult.Writer {
    return FeedResult.Writer(
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

fun FeedEntity.toFeedCard(userId: Long): FeedCard {
    val writer = this.userEntity!!
    return FeedCard(
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

fun UserEntity.toFeedCardWriter(): FeedCard.Writer {
    return FeedCard.Writer(
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
fun List<Marathon>.toSearchAllMarathonResponse(): SearchAllMarathonResponse {
    val marathons = this.map { it.toSearchAllMarathonResponseMarathon() }
    return SearchAllMarathonResponse(marathons = marathons)
}

fun Marathon.toSearchAllMarathonResponseMarathon(): SearchAllMarathonResponse.Marathon {
    return SearchAllMarathonResponse.Marathon(
        id = this.id,
        title = this.title,
        schedule = this.schedule,
        venue = this.venue,
        course = this.course,
        isBookmarking = this.isBookmarking == true
    )
}

fun MarathonDetail.toSearchMarathonDetailResponse(): SearchMarathonDetailResponse {
    return SearchMarathonDetailResponse(
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

fun SearchAllMarathonRequest.toSearchAllMarathonCommand(userId: Long): SearchAllMarathonCommand {
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

fun List<MarathonEntity>.toMarathons(userId: Long): List<Marathon> {
    return this.map { it.toMarathon(userId) }
}

fun MarathonEntity.toMarathon(userId: Long): Marathon {
    return Marathon(
        id = this.id,
        title = this.title,
        schedule = this.schedule,
        venue = this.venue,
        course = this.course,
        isBookmarking = this.marathonBookmarkEntities.any { userId == it.userId }
    )
}

fun MarathonEntity.toMarathonDetail(userId: Long): MarathonDetail {
    return MarathonDetail(
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
fun ObjectStorage.toUploadFileResponse(): UploadFileResponse {
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

fun ObjectStorageEntity.toObjectStorage(): ObjectStorage {
    return ObjectStorage(
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
fun UserPrivacy.toSearchUserPrivacyResponse(): SearchUserPrivacyResponse {
    return SearchUserPrivacyResponse(
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

fun UserPrivacyEntity.toUserPrivacy(): UserPrivacy {
    return UserPrivacy(
        id = this.id,
        userId = this.userId,
        isInstagramIdPublic = this.isInstagramIdPublic
    )
}

// user refresh token
fun CreateRefreshTokenCommand.toCreateRefreshTokenQuery(userEntity: UserEntity): CreateRefreshTokenQuery {
    return CreateRefreshTokenQuery(
        userId = this.userId,
        refreshToken = this.refreshToken,
        userEntity = userEntity,
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
