package kr.kro.btr.adapter.out.persistence.querydsl

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.JPAExpressions
import com.querydsl.jpa.impl.JPAQueryFactory
import kr.kro.btr.domain.constant.ActivityRecruitmentType
import kr.kro.btr.domain.entity.ActivityEntity
import kr.kro.btr.domain.entity.QActivityEntity
import kr.kro.btr.domain.entity.QActivityImageMappingEntity
import kr.kro.btr.domain.entity.QActivityParticipationEntity
import kr.kro.btr.domain.entity.QCrewEntity
import kr.kro.btr.domain.entity.QObjectStorageEntity
import kr.kro.btr.domain.entity.QUserEntity
import kr.kro.btr.domain.entity.QUserPrivacyEntity
import kr.kro.btr.infrastructure.model.SearchAllActivityQuery
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class ActivityQuery(
    private val queryFactory: JPAQueryFactory
) {
    fun searchAllByFilter(query: SearchAllActivityQuery): List<ActivityEntity> {
        val activity = QActivityEntity.activityEntity

        // 크루에 소속되지 않은 사용자는 빈 리스트 반환
        if (query.myCrewId == null) {
            return emptyList()
        }

        var whereClause: BooleanExpression = activity.userEntity.crewId.eq(query.myCrewId)
        var optionalWhereClause: BooleanExpression? = null

        if (query.courses?.isNotEmpty() == true) {
            for (course in query.courses) {
                optionalWhereClause = optionalWhereClause?.or(activity.course.contains(course))
                    ?: activity.course.contains(course)
            }

            whereClause = whereClause.and(optionalWhereClause)
        }

        query.recruitmentType?.let { recruitmentType ->
            val now = LocalDateTime.now()
            val subQueryParticipation = QActivityParticipationEntity("subParticipation")

            when (recruitmentType) {
                ActivityRecruitmentType.CLOSED -> {
                    whereClause = whereClause
                        .and(activity.isOpen.isTrue)
                        .and(activity.startAt.before(now))
                }
                ActivityRecruitmentType.RECRUITING -> {
                    whereClause = whereClause
                        .and(activity.isOpen.isFalse)
                        .and(activity.startAt.after(now))
                }
                ActivityRecruitmentType.FULL -> {
                    val participantsCountSubQuery = JPAExpressions
                        .select(subQueryParticipation.count().intValue())
                        .from(subQueryParticipation)
                        .where(subQueryParticipation.activityId.eq(activity.id))

                    whereClause = whereClause
                        .and(activity.isOpen.isFalse)
                        .and(activity.startAt.after(now))
                        .and(activity.participantsLimit.eq(participantsCountSubQuery))
                }
                ActivityRecruitmentType.ALREADY_PARTICIPATING -> {
                    val existsSubQuery = JPAExpressions
                        .selectFrom(subQueryParticipation)
                        .where(
                            subQueryParticipation.activityId.eq(activity.id)
                                .and(subQueryParticipation.userId.eq(query.myUserId))
                        )

                    whereClause = whereClause.and(existsSubQuery.exists())
                }
            }
        }

        val activityParticipation = QActivityParticipationEntity.activityParticipationEntity
        val activityImageMapping = QActivityImageMappingEntity.activityImageMappingEntity
        val user = QUserEntity.userEntity
        val crew = QCrewEntity.crewEntity
        val userPrivacy = QUserPrivacyEntity.userPrivacyEntity
        val profileImageStorage = QObjectStorageEntity("profileImageStorage")
        val activityImageStorage = QObjectStorageEntity("activityImageStorage")

        val activities = queryFactory
            .selectFrom(activity)
            .innerJoin(activity.userEntity, user).fetchJoin()
            .leftJoin(user.userPrivacyEntity, userPrivacy).fetchJoin()
            .leftJoin(user.profileImageEntity, profileImageStorage).fetchJoin()
            .leftJoin(user.crewEntity, crew).fetchJoin()
            .where(whereClause)
            .orderBy(activity.id.asc())
            .fetch()

        if (activities.isEmpty()) {
            return activities
        }

        val activityIds = activities.map { it.id }

        queryFactory
            .selectFrom(activityParticipation)
            .leftJoin(activityParticipation.userEntity, user).fetchJoin()
            .leftJoin(user.crewEntity, crew).fetchJoin()
            .leftJoin(user.profileImageEntity, profileImageStorage).fetchJoin()
            .where(activityParticipation.activityId.`in`(activityIds))
            .fetch()

        queryFactory
            .selectFrom(activityImageMapping)
            .leftJoin(activityImageMapping.objectStorageEntity, activityImageStorage).fetchJoin()
            .where(activityImageMapping.activityId.`in`(activityIds))
            .fetch()

        return activities
    }
}
