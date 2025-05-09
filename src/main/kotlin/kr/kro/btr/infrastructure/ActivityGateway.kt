package kr.kro.btr.infrastructure

import kr.kro.btr.adapter.out.persistence.ActivityParticipationRepository
import kr.kro.btr.adapter.out.persistence.ActivityRepository
import kr.kro.btr.adapter.out.persistence.querydsl.ActivityQuery
import kr.kro.btr.adapter.out.thirdparty.RedisClient
import kr.kro.btr.core.converter.ActivityConverter
import kr.kro.btr.domain.constant.ActivityRecruitmentType.*
import kr.kro.btr.domain.entity.ActivityEntity
import kr.kro.btr.domain.entity.ActivityParticipationEntity
import kr.kro.btr.infrastructure.model.AttendanceActivityQuery
import kr.kro.btr.infrastructure.model.CreateActivityQuery
import kr.kro.btr.infrastructure.model.ModifyActivityQuery
import kr.kro.btr.infrastructure.model.ParticipateActivityQuery
import kr.kro.btr.infrastructure.model.SearchAllActivityQuery
import kr.kro.btr.support.exception.InvalidException
import kr.kro.btr.support.exception.NotFoundException
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import kotlin.random.Random
import kotlin.time.Duration.Companion.minutes
import kotlin.time.toJavaDuration

@Component
class ActivityGateway(
    private val activityConverter: ActivityConverter,
    private val activityRepository: ActivityRepository,
    private val activityParticipationRepository: ActivityParticipationRepository,
    private val activityQuery: ActivityQuery,
    private val redisClient: RedisClient
) {

    companion object {
        private const val ACCESS_CODE_KEY_PREFIX = "accessCode"
    }

    fun create(query: CreateActivityQuery) {
        val activityEntity = activityRepository.findByStartAtAndUserId(query.startAt, query.myUserId) ?: activityConverter.map(query)
        activityRepository.save(activityEntity)
    }

    fun modify(query: ModifyActivityQuery) {
        val activityEntity = search(query.activityId)
        activityEntity.modify(query)
        activityRepository.save(activityEntity)
    }

    fun removeAll(userId: Long) {
        val participationIds = activityParticipationRepository.findAllByUserId(userId)
            .map { it.id }
        activityParticipationRepository.deleteAllById(participationIds)

        val activityIds = activityRepository.findAllByUserId(userId)
            .map { it.id }
        activityRepository.deleteAllById(activityIds)
    }

    fun remove(activityId: Long) {
        activityRepository.deleteById(activityId)
    }

    fun participate(participateActivityQuery: ParticipateActivityQuery) {
        val activityParticipationEntity = activityConverter.map(participateActivityQuery)
        activityParticipationRepository.save(activityParticipationEntity)
    }

    fun participateCancel(participationId: Long) {
        activityParticipationRepository.deleteById(participationId)
    }

    fun searchAll(query: SearchAllActivityQuery): List<ActivityEntity> {
        val activityEntities = activityQuery.searchAllByFilter(query)

        query.recruitmentType?.let {
            return when (it) {
                RECRUITING -> activityEntities.filter { a ->
                    a.participantsLimit > a.activityParticipationEntities.size
                }
                ALREADY_PARTICIPATING -> activityEntities.filter { a ->
                    a.activityParticipationEntities.any { ap ->
                        ap.userEntity?.id == query.myUserId
                    }
                }
                ENDED -> activityEntities.filter { a ->
                    a.startAt.isBefore(LocalDateTime.now())
                }
                CLOSED -> activityEntities.filter { a ->
                    a.participantsLimit <= a.activityParticipationEntities.size
                }
            }
        }

        return activityEntities
    }

    fun search(activityId: Long): ActivityEntity {
        return activityRepository.findByIdOrNull(activityId) ?: throw NotFoundException("모임을 찾지 못했습니다.");
    }

    fun open(activityId: Long): ActivityEntity {
        val activity = search(activityId)
        if (activity.isOpen) {
            throw InvalidException("이미 오픈하였습니다.")
        }
        val now = LocalDateTime.now()

        if (now.isAfter(activity.startAt.minusMinutes(10L)) && now.isBefore(activity.startAt.plusMonths(10L))) {
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

    fun initAccessCode(activityId: Long): Int {
        val accessCode = Random.nextInt(1, 101)  // 1부터 100까지 난수
        redisClient.save(ACCESS_CODE_KEY_PREFIX + activityId, accessCode, 5.minutes.toJavaDuration())
        return accessCode
    }

    fun searchParticipation(activityId: Long): List<ActivityParticipationEntity> {
        return activityParticipationRepository.findAllByActivityId(activityId)
    }
}

