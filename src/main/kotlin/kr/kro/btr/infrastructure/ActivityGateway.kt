package kr.kro.btr.infrastructure

import kr.kro.btr.adapter.out.persistence.ActivityImageMappingRepository
import kr.kro.btr.adapter.out.persistence.ActivityParticipationRepository
import kr.kro.btr.adapter.out.persistence.ActivityRepository
import kr.kro.btr.adapter.out.persistence.querydsl.ActivityQuery
import kr.kro.btr.adapter.out.thirdparty.RedisClient
import kr.kro.btr.base.extension.toActivityEntity
import kr.kro.btr.base.extension.toActivityParticipationEntity
import kr.kro.btr.domain.entity.ActivityEntity
import kr.kro.btr.domain.entity.ActivityImageMappingEntity
import kr.kro.btr.domain.entity.ActivityParticipationEntity
import kr.kro.btr.domain.model.ModifyActivityQuery
import kr.kro.btr.infrastructure.model.ActivityAggregationData
import kr.kro.btr.infrastructure.model.AttendanceActivityQuery
import kr.kro.btr.infrastructure.model.CreateActivityQuery
import kr.kro.btr.infrastructure.model.ParticipateActivityQuery
import kr.kro.btr.infrastructure.model.SearchAllActivityQuery
import kr.kro.btr.support.exception.ForBiddenException
import kr.kro.btr.support.exception.InvalidException
import kr.kro.btr.support.exception.NotFoundException
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import kotlin.random.Random
import kotlin.time.Duration.Companion.minutes
import kotlin.time.toJavaDuration

@Component
class ActivityGateway(
    private val activityRepository: ActivityRepository,
    private val activityImageMappingRepository: ActivityImageMappingRepository,
    private val activityParticipationRepository: ActivityParticipationRepository,
    private val activityQuery: ActivityQuery,
    private val redisClient: RedisClient
) {

    fun create(query: CreateActivityQuery): Long {
        val activityEntity = activityRepository.findByStartAtAndUserId(query.startAt, query.myUserId) ?: query.toActivityEntity()
        activityRepository.save(activityEntity)

        val activityImageMappingEntities = query.imageIds?.map { imageId ->
            ActivityImageMappingEntity(
                imageId = imageId,
                activityId = activityEntity.id
            )
        }

        activityEntity.add(activityImageMappingEntities)
        if (activityImageMappingEntities != null) {
            activityImageMappingRepository.saveAll(activityImageMappingEntities)
        }

        return activityEntity.id
    }

    fun modify(query: ModifyActivityQuery) {
        val activityEntity = search(query.activityId)
        activityEntity.modify(query)
        activityRepository.save(activityEntity)
    }

    fun remove(activityId: Long) {
        activityRepository.deleteById(activityId)
    }

    fun participate(query: ParticipateActivityQuery) {
        if (!activityRepository.existsById(query.activityId)) {
            throw NotFoundException("행사를 찾을 수 없습니다.")
        }

        val activityParticipationEntity = activityParticipationRepository.findByActivityIdAndUserId(query.activityId, query.myUserId) ?: query.toActivityParticipationEntity()
        activityParticipationRepository.save(activityParticipationEntity)
    }

    fun participateCancel(participationId: Long) {
        activityParticipationRepository.deleteById(participationId)
    }

    fun searchAll(query: SearchAllActivityQuery): List<ActivityEntity> {
        return activityQuery.searchAllByFilter(query)
    }

    fun searchAllWithAggregation(query: SearchAllActivityQuery, userId: Long): List<kr.kro.btr.infrastructure.model.ActivityAggregationData> {
        val activities = searchAll(query)
        if (activities.isEmpty()) return emptyList()

        val activityIds = activities.map { it.id }

        val participationCountMap = activityParticipationRepository.countGroupByActivityId(activityIds)
            .associate {
                (it["activityId"] as Long) to ((it["count"] as Long).toInt())
            }

        val userParticipatedIds = activityParticipationRepository
            .findUserParticipatedActivityIds(userId, activityIds)
            .toSet()

        return activities.map { activity ->
            kr.kro.btr.infrastructure.model.ActivityAggregationData(
                activityEntity = activity,
                participantsCount = participationCountMap[activity.id] ?: 0,
                hasUserParticipation = activity.id in userParticipatedIds
            )
        }
    }

    fun search(activityId: Long): ActivityEntity {
        return activityRepository.findByIdOrNull(activityId) ?: throw NotFoundException("모임을 찾지 못했습니다.");
    }

    fun searchWithAggregation(activityId: Long, userId: Long): kr.kro.btr.infrastructure.model.ActivityAggregationData {
        val activity = search(activityId)

        val participantsCount = activityParticipationRepository.countByActivityId(activityId)
        val hasUserParticipation = activityParticipationRepository.existsByActivityIdAndUserId(activityId, userId)

        val accessCodeInfo = getAccessCode(activityId)

        return ActivityAggregationData(
            activityEntity = activity,
            participantsCount = participantsCount,
            hasUserParticipation = hasUserParticipation,
            attendanceCode = accessCodeInfo?.first,
            attendanceExpiresAt = accessCodeInfo?.second
        )
    }

    fun open(activityId: Long, requestUserId: Long): ActivityEntity {
        val activity = search(activityId)

        if (activity.userId != requestUserId) {
            throw ForBiddenException("모임 호스트만 오픈할 수 있습니다.")
        }

        if (activity.isOpen) {
            throw InvalidException("이미 오픈하였습니다.")
        }
        val now = LocalDateTime.now()
        val openStartTime = activity.startAt.minusMinutes(10L)
        val openEndTime = activity.startAt.plusHours(2L)

        if (!now.isBefore(openStartTime) && now.isBefore(openEndTime)) {
            activity.open()
            return activityRepository.save(activity)
        }

        throw InvalidException("모임 일정 외에는 오픈할 수 없습니다.")
    }

    fun attendance(attendanceActivityQuery: AttendanceActivityQuery) {
        val accessCodeKey = ACCESS_CODE_KEY_PREFIX + attendanceActivityQuery.activityId
        if (!redisClient.exist(accessCodeKey)) {
            throw InvalidException("참여코드가 만료되었습니다.")
        }

        val storedAccessCode = redisClient.get(accessCodeKey) as Int
        if (storedAccessCode == attendanceActivityQuery.accessCode) {
            val activityParticipation = activityParticipationRepository.findByActivityIdAndUserId(
                attendanceActivityQuery.activityId, attendanceActivityQuery.myUserId
            ) ?: throw NotFoundException("참여의사를 밝히지 않은 모임에 출석할 수 없습니다.")
            activityParticipation.attendance()
            activityParticipationRepository.save(activityParticipation)
        } else {
            throw InvalidException("참여코드가 일치하지 않습니다.")
        }
    }

    fun initAccessCode(activityId: Long): Pair<Int, LocalDateTime> {
        val accessCode = Random.nextInt(1000, 10000)  // 1000부터 9999까지 4자리 난수
        val expiresAt = LocalDateTime.now().plusMinutes(5)
        redisClient.save(ACCESS_CODE_KEY_PREFIX + activityId, accessCode, 5.minutes.toJavaDuration())
        return Pair(accessCode, expiresAt)
    }

    fun getAccessCode(activityId: Long): Pair<Int, LocalDateTime>? {
        val accessCodeKey = ACCESS_CODE_KEY_PREFIX + activityId
        if (!redisClient.exist(accessCodeKey)) {
            return null
        }

        val accessCode = redisClient.get(accessCodeKey) as? Int ?: return null
        val ttl = redisClient.ttl(accessCodeKey)

        if (ttl <= 0) {
            return null
        }

        val expiresAt = LocalDateTime.now().plusSeconds(ttl)
        return Pair(accessCode, expiresAt)
    }

    fun searchParticipation(activityId: Long): List<ActivityParticipationEntity> {
        return activityParticipationRepository.findAllByActivityId(activityId)
    }

    fun searchMyParticipations(myUserId: Long): List<ActivityEntity> {
        val participations = activityParticipationRepository.findAllByUserId(myUserId)
        return participations.mapNotNull { it.activityEntity }
    }

    fun getParticipationCountMap(activityIds: List<Long>): Map<Long, Int> {
        if (activityIds.isEmpty()) return emptyMap()
        return activityParticipationRepository.countGroupByActivityId(activityIds)
            .associate {
                (it["activityId"] as Long) to ((it["count"] as Long).toInt())
            }
    }

    companion object {
        private const val ACCESS_CODE_KEY_PREFIX = "accessCode"
    }
}

